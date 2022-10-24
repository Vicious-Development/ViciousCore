package com.vicious.viciouscore.aunotamation;

import com.vicious.viciouslib.aunotamation.AnnotationProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class VCoreProcessor<A extends Annotation,T> extends AnnotationProcessor<A, T> {
    public VCoreProcessor(Class<A> aClass, Class<T> applyON) {
        super(aClass, applyON);
    }
    protected Object requireInitialized(Field f, Object src) throws IllegalAccessException {
        Object o = f.get(src);
        if (o == null) err(f,"must be initialized on declaration!");
        return o;
    }
}
