package com.vicious.viciouscore.common.util.reflect;

import java.lang.reflect.Field;

/**
 * Used to copy EVERY field in one object to the fields in another, assuming the other object is a child of or is an instance of the original object.
 */
public interface IFieldCloner {
    default void clone(Object og){
        Class<?> clazz = og.getClass();
        while(clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                Field f2 = Reflection.getField(this, f.getName());
                try {
                    f2.setAccessible(true);
                    f2.set(this, Reflection.accessField(og, f));
                } catch (IllegalAccessException ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
    }
    static void clone(Object clone, Object og){
        Class<?> clazz = og.getClass();
        while(clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                Field f2 = Reflection.getField(clone, f.getName());
                try {
                    f2.setAccessible(true);
                    f2.set(clone, Reflection.accessField(og, f));
                } catch (IllegalAccessException ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
    }
}
