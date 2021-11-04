package com.vicious.viciouscore.common.util.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * A class designed to go down a rabbit hole of fields starting at a static one. Usually this only needs to start at one field and end there.
 */
public class FieldRetrievalRoute {
    final String classTarget;
    final String[] fieldTargets;

    public FieldRetrievalRoute(String classTarget, String... fieldTargets) {
        this.classTarget = classTarget;
        this.fieldTargets = fieldTargets;
    }

    /**
     * @return the object in the final field or null if a class or field in the route doesn't exist.
     */
    public Object getEndValue() {
        Object obj = null;
        try {
            Class<?> clazz = Class.forName(classTarget);
            //Get the value first field. This MUST be static for the system to obtain it.
            Field f;
            for (int i = 0; i < fieldTargets.length; i++) {
                f = Reflection.getField(clazz, fieldTargets[i]);
                if(f == null) return null;
                obj = Reflection.accessField(f,clazz);
                if(obj == null) return null;
                clazz = obj.getClass();
            }
        } catch (ClassNotFoundException ex) {
            return null;
        }
        return obj;
    }

    /**
     * @return the object that is of the class of the final field or null if a class or field in the route doesn't exist.
     */
    public Object getEndObjectSupplier() {
        Object obj=null;
        try {
            Class<?> clazz = Class.forName(classTarget);
            obj = clazz;
            //Get the value first field. This MUST be static for the system to obtain it.
            Field f;
            for (int i = 0; i < fieldTargets.length-1; i++) {
                f = Reflection.getField(clazz, fieldTargets[i]);
                if(f == null) return null;
                obj = Reflection.accessField(f,clazz);
                if(obj == null) return null;
                clazz = obj.getClass();
            }
        } catch (ClassNotFoundException ex) {
            return null;
        }
        return obj;
    }

    public String endClass() {
        return classTarget;
    }

    public String endField() {
        return fieldTargets[fieldTargets.length - 1];
    }
    public String toString(){
        return classTarget + ": " + Arrays.toString(fieldTargets);
    }
}
