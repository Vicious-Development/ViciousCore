package com.vicious.viciouscore.common.util.reflect;

import java.lang.reflect.Method;

public class ShadowMethod<T> {
    private Method reflectiveMethod;
    public ShadowMethod(Class<?> cls,String methodname, Class<?>... params){
        reflectiveMethod = Reflection.getMethod(cls,methodname,params);
    }
    public T invoke(Object toAccess, Object... args){
        Object ret = (T) Reflection.invokeMethod(toAccess,reflectiveMethod,args);
        return ret == null ? null : (T)ret;
    }
}
