package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * The overrider can be used to modify the structure of other mods and apply fixes by injecting custom data into fields.
 * There are a few events on which an override will be executed.
 * Calls to Overrider.registerPreInitInjector should be made after VCore and whatever overridden mod has initialized.
 */
@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class Overrider {
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> preInit = new HashMap<>();
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> init = new HashMap<>();
    private static Map<FieldRetrievalRoute, OverrideConverter<?>> postInit = new HashMap<>();
    private static Map<ResourceLocation, Block> blockRegReplacement = new HashMap<>();

    /**
     * Called when VCore starts preinitialization.
     * @param route route of classes and fields to go through to access the target field via an object.
     * @param overriddenVariantFactory converts the original variant to the overridden variant.
     */
    public static void registerPreInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        preInit.putIfAbsent(route, overriddenVariantFactory);
    }

    public static void registerInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        init.putIfAbsent(route, overriddenVariantFactory);
    }

    public static void registerPostInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        postInit.putIfAbsent(route, overriddenVariantFactory);
    }

    /**
     * Warning! Replacing registry entries is something that has never been done before outside of VCore as far as I know.
     * Be sure to inject the replacement block into any fields where the original block was.
     * There are likely unknown risks. Be careful.
     */
    public static void registerBlockRegistryReplacer(ResourceLocation rl, Block block) {
        blockRegReplacement.putIfAbsent(rl, block);
    }

    @SubscribeEvent
    public static void onBlockRegistration(RegistryEvent.Register<Block> forgereg){
        ForgeRegistry<Block> frg = (ForgeRegistry<Block>) forgereg.getRegistry();
        blockRegReplacement.forEach((rl,blk)->{
            if(frg.containsKey(rl)) {
                Block b1 = frg.remove(rl);
                blk.setRegistryName(b1.getRegistryName());
                frg.register(blk);
            }
        });
    }

    public static void onPreInit(){
        processOverrideMap(preInit);
        preInit=null;
    }
    public static void onInit(){
        processOverrideMap(init);
        init=null;
    }
    public static void onPostInit(){
        processOverrideMap(postInit);
        postInit=null;
    }
    /*

    Did not complete. This method is literally too dangerous to give anyone access to. Even myself. Overwriting tiles in the TE registry can delete their data on world load.
       Use TileEntityOverrider instead.
    @SubscribeEvent
    @SuppressWarnings({"unchecked","rawtypes"})
    public static void onTileEntityRegistration(){
        RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> reg = (RegistryNamespaced) Reflection.accessField(TileEntity.class,"REGISTRY");
        blockReg.forEach((rr, bo)->{
            if(reg.containsKey(rr.resource)){
                Block b = reg.remove(rr.resource);
                Block overridden = bo.override(b);
                reg.register(b);
                processRegistrationOverride(overridden,rr.route);
            }
        });
    }*/

    private static void inject(Object overridden, FieldRetrievalRoute route){
        if(route != null) {
            Object objectToAccess = route.getEndObjectSupplier();
            if (objectToAccess != null) Reflection.setField(objectToAccess,overridden,route.endField());
        }
    }
    @FunctionalInterface
    public interface OverrideConverter<T>{
        T override(Object in);
    }
    private static void processOverrideMap(Map<FieldRetrievalRoute,OverrideConverter<?>> map){
        map.forEach((route, converter)->{
            try {
                Class<?> clazz = Class.forName(route.endClass());
                Field f = Reflection.getField(clazz,route.endField());
                if(f != null) {
                    Object toOverride = Reflection.accessField(f, route.getEndValue());
                    toOverride = converter.override(toOverride);
                    Object objectToAccess = route.getEndObjectSupplier();
                    if (toOverride != null && objectToAccess != null)
                        Reflection.setField(objectToAccess, toOverride, f.getName());
                    else{
                        System.out.println("Not overriding " + route + " because the final object was null or the override object was null. \nFIELD: " + f.getName() + "\nTARGET: " + objectToAccess + "\nOVERRIDE" + toOverride);
                    }
                }
                else{
                    System.out.println("Not overriding " + route + " because one of the route fields was missing.");
                }
            } catch(ClassNotFoundException ignored){
                System.out.println("Not overriding " + route + " because one of the route classes was missing.");
                //Mod isn't installed, don't finish execution.
            }
        });
    }
}
