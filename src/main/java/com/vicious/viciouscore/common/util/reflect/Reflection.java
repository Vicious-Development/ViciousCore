package com.vicious.viciouscore.common.util.reflect;

import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import com.vicious.viciouslib.serialization.SerializationUtil;

import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

    /**
     * Allows accessing private nonstatic FIELDS in an Object.
     * @param accessed = the object.
     * @param fieldname = the field.
     * @return uncasted field data
     */
    public static Object accessField(Object accessed, String fieldname){
        Field f = getField(accessed,fieldname);
        if(f != null){
            try{
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                return f.get(accessed);
            } catch(IllegalAccessException e){

            }
        }
        return null;
    }

    public static Object accessField(Object obj, Field f){
        if(f != null){
            try{
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                return f.get(obj);
            } catch(IllegalAccessException e){

            }
        }
        return null;
    }
    public static Method getMethod(Object accessed, String methodname, Class<?>[] parameters){
        Class<?> clazz = accessed instanceof Class<?> ? (Class<?>)accessed : accessed.getClass();
        return SerializationUtil.executeOnTargetClass((cls)->{
            String target = methodname;
            if(MappingsReference.hasMappingForClass(cls)){
                MappingsReference.Mapping mapping = MappingsReference.getMapping(cls);
                if(mapping.hasSRG(methodname)){
                    target = mapping.getObfuscated(methodname);
                }
            }
            try {
                return cls.getDeclaredMethod(target,parameters);
            } catch (NoSuchMethodException ignored) {
                try {
                    return cls.getDeclaredMethod(methodname,parameters);
                } catch (NoSuchMethodException ignored1) {
                    return null;
                }
            }
        } ,clazz);
    }
    public static Object invokeMethod(Object accessed, String methodname, Class<?>[] parameters, Object[] args){
        Method m = getMethod(accessed,methodname,parameters);
        try {
            if(!m.isAccessible()) m.setAccessible(true);
            if (m.getReturnType() == void.class) {
                m.invoke(accessed, args);
            } else return m.invoke(accessed, args);
        } catch(IllegalAccessException | InvocationTargetException ignored){}
        return null;
    }
    public static Object invokeMethod(Object accessed, Method m, Object[] args){
        try {
            if(!m.isAccessible()) m.setAccessible(true);
            if (m.getReturnType() == void.class) {
                m.invoke(accessed, args);
            } else return m.invoke(accessed, args);
        } catch(IllegalAccessException | InvocationTargetException ignored){}
        return null;
    }
    //Disabled for the time being due to java not supporting this, even via reflection.
    //Future solution: create a super duper object, clone the original object's fields into the superduper. Run the method. Clone the fields from the superduper to the child.
    /*public static Object invokeSuperDuperMethod(Object accessed, Class<?> superClass, Class<?>[] parameterTypes, Object[] args, String methodName) {
        Method m = getMethod(supgierClass,methodName,parameterTypes);
        try {
            if (m.getReturnType() == void.class) {
                m.invoke(accessed, args);
            } else return m.invoke(accessed, args);
        } catch(IllegalAccessException | InvocationTargetException ignored){}
        return null;
    }*/
    public static Field getField(Object accessed, String fieldname) {
        Class<?> clazz = accessed instanceof Class<?> ? (Class<?>) accessed : accessed.getClass();
        return SerializationUtil.executeOnTargetClass((cls)->{
            String target = fieldname;
            if(MappingsReference.hasMappingForClass(cls)){
                MappingsReference.Mapping mapping = MappingsReference.getMapping(cls);
                if(mapping.hasSRG(fieldname)){
                    target = mapping.getObfuscated(fieldname);
                }
            }
            try {
                return cls.getDeclaredField(target);
            } catch (NoSuchFieldException ignored) {
                try {
                    return cls.getDeclaredField(fieldname);
                } catch (NoSuchFieldException ignored1) {
                    return null;
                }
            }
        } ,clazz);
    }

    /**
     * Pure evil. Time to start fucking with things you shouldn't eh?
     * In a separate method so those who desire to change final fields have to intentionally.
     */
    public static void definalize(Object accessed, String fieldname){
        Field f = getField(accessed,fieldname);
        if(f != null){
            try{
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                Field finfield = Field.class.getDeclaredField("modifiers");
                finfield.setAccessible(true);
                finfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

            } catch(IllegalAccessException | NoSuchFieldException ignored){

            }
        }
    }
    private static void outputClass(Class<?> clazz) {
        String path = ViciousDirectories.rootDir()+"/classOPT.txt";
        FileUtil.createDNE(path);
        try {
            Files.write(Paths.get(path), clazz.getName().getBytes(), StandardOpenOption.APPEND);
        } catch(Exception ignored){}
    }

    public static void setField(Object accessed, Object value, String fieldname){
        Field f = getField(accessed, fieldname);
        if (f != null) {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                f.set(accessed, value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }
    public static void setField(Object accessed, Object value, Field f){
        if (f != null) {
            try {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                f.set(accessed, value);
            } catch (IllegalAccessException ignored) {
            }
        }
    }
    public static Constructor<?> getConstructor(Class<?> accessed, Class<?>[] params){
        try {
            return accessed.getConstructor(params);
        } catch(NoSuchMethodException ignored){}
        return null;
    }
    public static Object accessStaticField(Class<?> accessed, String fieldname){
        Field f = null;
        try {
            f = accessed.getDeclaredField(fieldname);
        } catch(NoSuchFieldException ignored){}
        if(f != null){
            try{
                return f.get(accessed);
            } catch(IllegalAccessException ignored){}
        }
        return null;
    }


    /**
     * The following two methods were intended to allow us to access private INNER classes. It has a problem though. Constructing a class from to the desired private class is practically impossible.
     * I'm leaving this here in case I find a solution or this code becomes useful.
     *
     */
    public static <T> Class<?> accessClass(Class<T> accessed, String className) {
        Class<?>[] classes = accessed.getDeclaredClasses();
        for(Class<?> cls : classes){
            if(cls.getName().equals(className)){
                return cls;
            }
        }
        return null;
    }
    public static Object construct(Constructor<?> constructor, Object[] params){
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Field> getFieldsOfType(Class<?> clazz, Class<?> type) {
        List<Field> fields = new ArrayList<>();
        while(clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.getType().equals(type)) fields.add(declaredField);
                if(declaredField.getType().getSuperclass() != null && declaredField.getType().getSuperclass().equals(type)) fields.add(declaredField);
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    public static Field getFieldContaining(Object object, Class<?> type, Object toFind) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = object.getClass();
        while(clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.getType().equals(type)) fields.add(declaredField);
                if(declaredField.getType().getSuperclass() != null && declaredField.getType().getSuperclass().equals(type)) fields.add(declaredField);
            }
            clazz = clazz.getSuperclass();
        }
        for (Field field : fields) {
            if(!field.isAccessible()) field.setAccessible(true);
            try {
                if (field.get(object) == toFind) return field;
            } catch(IllegalAccessException ignored){}
        }
        return null;
    }

    public static String fieldsToString(Object object) {
        Class<?> clazz = object.getClass();
        String fields = "";
        while(clazz != null){
            for (Field declaredField : clazz.getDeclaredFields()) {
                fields += "\n" + declaredField;
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
