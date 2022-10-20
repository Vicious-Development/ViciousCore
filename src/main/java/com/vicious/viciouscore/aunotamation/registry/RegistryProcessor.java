package com.vicious.viciouscore.aunotamation.registry;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.aunotamation.registry.annotation.Associations;
import com.vicious.viciouscore.aunotamation.registry.annotation.Register;
import com.vicious.viciouscore.aunotamation.registry.annotation.Registry;
import com.vicious.viciouslib.aunotamation.AnnotationProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public abstract class RegistryProcessor<T,O,A> {
    private final Map<String, DeferredRegister<O>> regs = new HashMap<>();
    private final Class<?>[] cparams;
    private final Class<T> targetType;
    private final IForgeRegistry<A> associationsRegistry;
    private final IForgeRegistry<O> targetRegistry;
    public RegistryProcessor(@NotNull Class<T> targetType, IForgeRegistry<O> targetRegistry, @Nullable IForgeRegistry<A> associationsRegistry, Class<?>... constructorParams){
        this.targetType=targetType;
        this.associationsRegistry=associationsRegistry;
        this.targetRegistry=targetRegistry;
        this.cparams=constructorParams;
    }
    private AnnotationProcessor<?,?> processor;

    public AnnotationProcessor<?,?> getAnnotationProcessor(){
        return processor;
    }
    public void setAnnotationProcessor(AnnotationProcessor<?,?> processor){
        this.processor=processor;
    }

    protected DeferredRegister<O> getDeferredRegistry(Class<?> regClass){
        Registry annotation = regClass.getAnnotation(Registry.class);
        if(annotation == null){
            processor.err(regClass, "must have @Registry annotation.");
        }
        return getOrCreate(annotation.value());
    }
    protected DeferredRegister<O> getOrCreate(String modid){
        if(regs.containsKey(modid)){
            return regs.get(modid);
        }
        else{
            DeferredRegister<O> reg = DeferredRegister.create(targetRegistry,modid);
            reg.register(FMLJavaModLoadingContext.get().getModEventBus());
            regs.put(modid,reg);
            return reg;
        }
    }
    public Class<T> getTargetClass(AnnotatedElement elem){
        Register r = elem.getAnnotation(Register.class);
        return (Class<T>) r.value();
    }
    protected void process(Class registryClass, Field f) throws Exception{
        DeferredRegister<O> deferredRegister = getDeferredRegistry(registryClass);
        //Get the tile class.
        Class<T> target = getTargetClass(f);
        //Get the BlockEntity Constructor.
        Constructor<T> constructor = getConstructor(target,f);
        List<A> associations;
        if(associationsRegistry == null){
            associations = new ArrayList<>();
        }
        else {
            //Get the targetClass' associations.
            associations = getAssociations(associationsRegistry, target);
        }
        RegistryObject<?> obj = deferredRegister.register(f.getName().toLowerCase(Locale.ROOT),()->{
            try {
                return supply(target,constructor,associations,f);
            }
            catch (Exception e){
                ViciousCore.logger.fatal("Could not register a " + targetType.getName() + " due to an internal exception.");
                e.printStackTrace();
            }
            return null;
        });
        f.set(registryClass,obj);
    }
    public Constructor<T> getConstructor(Class<T> targetClass, AnnotatedElement elem){
        try{
            return targetClass.getConstructor(cparams);
        }
        catch (Exception e){
            processor.err(elem,"linked class does not have a constructor with parameters: " + Arrays.toString(cparams));
        }
        return null;
    }
    public List<A> getAssociations(IForgeRegistry<A> reg, AnnotatedElement elem){
        Associations associations = elem.getAnnotation(Associations.class);
        if(associations == null) {
            processor.err(elem, "class is missing @Associations annotation with its respective associations");
        }
        String namespace = associations.namespace();
        String[] keys = associations.value();
        List<A> associatedRegisteredObjects = new ArrayList<>();
        for (String key : keys) {
            associatedRegisteredObjects.add(reg.getValue(new ResourceLocation(namespace, key)));
        }
        return associatedRegisteredObjects;
    }
    public abstract O supply(Class<T> targetClass, Constructor<T> constructor, List<A> associations, Field targetField) throws Exception;

    public Class<T> getTargetType() {
        return targetType;
    }
}
