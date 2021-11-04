package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.common.util.reflect.Reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * The overrider can be used to modify the structure of other mods and apply fixes by injecting custom data into fields.
 * There are a few events on which an override will be executed.
 * Calls to Overrider.registerPreInitInjector should be made after VCore and whatever overridden mod has initialized.
 */
public class Overrider {
    private static HashMap<FieldRetrievalRoute, OverrideConverter<?>> preInit = new HashMap<>();
    private static HashMap<FieldRetrievalRoute, OverrideConverter<?>> init = new HashMap<>();

    /**
     * Called when VCore starts preinitialization.
     * @param route route of classes and fields to go through to access the target field via an object.
     * @param overriddenVariantFactory converts the original variant to the overridden variant.
     */
    public static void registerPreInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        preInit.put(route, overriddenVariantFactory);
    }

    public static void registerInitInjector(FieldRetrievalRoute route, OverrideConverter<?> overriddenVariantFactory) {
        init.put(route, overriddenVariantFactory);
    }
    public static void onPreInit(){
        processOverrideMap(preInit);
        preInit=null;
    }
    public static void onInit(){
        processOverrideMap(init);
        init=null;
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
