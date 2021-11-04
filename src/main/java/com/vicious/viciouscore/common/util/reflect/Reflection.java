package com.vicious.viciouscore.common.util.reflect;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.renderer.entity.RenderLivingBase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static Object accessField(Field f, Object obj){
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
        Method m = null;
        //Try to find the field, regardless of hierarchy position.
        while(m == null && clazz != null) {
            try {
                m = clazz.getDeclaredMethod(methodname,parameters);
            } catch(NoSuchMethodException ignored){

            }
            clazz = clazz.getSuperclass();
        }
        return m;
    }
    public static Object invokeMethod(Object accessed, String methodname, Class<?>[] parameters, Object[] args){
        Method m = getMethod(accessed,methodname,parameters);
        try {
            if (m.getReturnType() == void.class) {
                m.invoke(accessed, args);
            } else return m.invoke(accessed, args);
        } catch(IllegalAccessException | InvocationTargetException ignored){}
        return null;
    }
    public static Object invokeMethod(Object accessed, Method m, Class<?>[] parameters, Object[] args){
        try {
            if (m.getReturnType() == void.class) {
                m.invoke(accessed, args);
            } else return m.invoke(accessed, args);
        } catch(IllegalAccessException | InvocationTargetException ignored){}
        return null;
    }
    public static Field getField(Object accessed, String fieldname) {
        Class<?> clazz = accessed instanceof Class<?> ? (Class<?>) accessed : accessed.getClass();
        Field f = null;
        //Try to find the field, regardless of hierarchy position.
        while (f == null && clazz != null) {
            String target = fieldname;
            if(MappingsReference.hasMappingForClass(clazz)){
                MappingsReference.Mapping mapping = MappingsReference.getMapping(clazz);
                if(mapping.hasSRG(fieldname)){
                    target = mapping.getObfuscated(fieldname);
                }
            }
            //In the dev env, this will take extra time, outside though we are in th clear.
            try {
                f = clazz.getDeclaredField(target);
            } catch (NoSuchFieldException ignored) {
                try {
                    f = clazz.getDeclaredField(fieldname);
                } catch (NoSuchFieldException ignored1) {}
            }
            clazz = clazz.getSuperclass();
        }
        if(f != null){
            if(ViciousCore.CFG.outputReflectionClasses.getBoolean()) outputClass(clazz);
            return f;
        }
        //If the code returns null, an obfuscation mapping HAS to be registered.
        return null;
    }

    private static void outputClass(Class<?> clazz) {
        String path = Directories.rootDir()+"/classOPT.txt";
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
    public static <T> T construct(Class<T> type, Object[] params){
        Class<?>[] types = new Class<?>[params.length];
        for(int i = 0; i < types.length; i++){
            types[i] = params[i].getClass();
        }
        try {
            return type.getConstructor(types).newInstance(params);
        }
        catch (Exception e){

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

    public static String fieldsToString(RenderLivingBase<?> entityRenderer) {
        Class<?> clazz = entityRenderer.getClass();
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
