package com.vicious.viciouscore.aunotamation.registry;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.aunotamation.registry.annotation.BlockAssociations;
import com.vicious.viciouscore.aunotamation.registry.annotation.LinkBE;
import com.vicious.viciouscore.aunotamation.registry.annotation.Registry;
import com.vicious.viciouslib.aunotamation.AnnotationProcessor;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import com.vicious.viciouslib.util.ClassAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;

public class RegistryAutomator {
    private static boolean init = false;
    public static void init() {
        if (init) return;
        init = true;
        //ALLOWS THIS:
        /*
        class ExampleTileRegistry{
            @Registry
            public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "example");
            @LinkBE(ExampleBE.class)
            public static RegistryObject<BlockEntityType<ExampleBE>> EXAMPLEBLOCKENTITY;
        }

        @BlockAssociations("exampleblock",namespace = "examplemod")
        class ExampleBE extends BlockEntity{...}
         */
        ViciousCore.logger.info("Registered Registry Automations.");
        Aunotamation.registerProcessor(new AnnotationProcessor<>(LinkBE.class, Class.class) {
            @Override
            @SuppressWarnings("unchecked")
            public void process(Class registryClass, AnnotatedElement annotatedElement) throws Exception {
                ViciousCore.logger.info("ANALYZING CLASS: " + registryClass.getName());
                if(annotatedElement instanceof Field f) {
                    //Get the DeferredRegister.
                    List<AnnotatedElement> possibilities = ClassAnalyzer.getManifest(registryClass).getMembersWithAnnotation(Registry.class);
                    if(possibilities.size() == 0){
                        err(annotatedElement,"there is no @Registry DeferredRegister");
                    }
                    if(possibilities.size() > 1){
                        err(annotatedElement,"there is more than one @Registry DeferredRegister");
                    }
                    DeferredRegister<BlockEntityType<?>> deferredRegister = (DeferredRegister<BlockEntityType<?>>) ((Field)possibilities.get(0)).get(registryClass);
                    //Get the tile class.
                    Class<?> tile = annotatedElement.getAnnotation(LinkBE.class).value();
                    try {
                        //Get the BlockEntity Constructor.
                        Constructor<?> constructor = tile.getConstructor(BlockPos.class, BlockState.class);
                        RegistryObject<?> obj = deferredRegister.register(f.getName().toLowerCase(Locale.ROOT),()->{
                            try {
                                //Get the tile's associated blocks.
                                BlockAssociations associations = tile.getAnnotation(BlockAssociations.class);
                                if(associations == null) {
                                    err(annotatedElement, " tile class is missing @BlockAssociations annotation");
                                }
                                String namespace = associations.namespace();
                                String[] keys = associations.value();
                                Block[] blocks = new Block[keys.length];
                                for (int i = 0; i < keys.length; i++) {
                                    blocks[i] = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(namespace, keys[i]));
                                }
                                //Create the block entity type.
                                return BlockEntityType.Builder.of((p, s) -> {
                                    Object be = null;
                                    try {
                                        be = constructor.newInstance(p, s);
                                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                                        err(annotatedElement, "encountered internal exception while creating new tile", e);
                                    }
                                    Aunotamation.processObject(be);
                                    return (BlockEntity) be;
                                }, blocks).build(null);
                            }
                            catch (Exception e){
                                ViciousCore.logger.fatal("Could register a block entity.");
                                e.printStackTrace();
                            }
                            return null;
                        });
                        System.err.println("OBJ: " + obj);
                        f.set(registryClass,obj);
                    }
                    catch (NoSuchMethodException e){
                        err(f,"encountered internal exception while registering tile",e);
                    }
                }
            }
        });
    }
}
