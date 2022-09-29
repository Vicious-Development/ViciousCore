package com.vicious.viciouscore.aunotamation.isyncablecompoundholder;

import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouslib.aunotamation.AnnotationProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public abstract class SyncProcessor<A extends Annotation> extends AnnotationProcessor<A, ISyncableCompoundHolder> {
    public SyncProcessor(Class<A> aClass, Class<ISyncableCompoundHolder> applyON) {
        super(aClass, applyON);
    }

    protected void ensureInCompound(ISyncableCompoundHolder compound, Field f){
        try {
            SyncableValue<?> o = (SyncableValue<?>) requireInitialized(f, compound);
            if (!compound.getData().containsKey(o.KEY)) {
                compound.getData().add((SyncableValue<?>) o);
            }
        }
        catch (IllegalAccessException e) {
            err(f,"must be declared public!");
        }
    }
    protected Object requireInitialized(Field f, Object src) throws IllegalAccessException {
        Object o = f.get(src);
        if (o == null) err(f,"must be initialized on declaration!");
        return o;
    }
    protected void requireInitialized(ISyncableCompoundHolder compound, AnnotatedElement anno, Consumer<SyncableValue<?>> cons) throws IllegalAccessException {
        if(anno instanceof Field f){
            if(requireInitialized(f, compound) instanceof SyncableValue<?> sv) {
                cons.accept(sv);
            }
        }
    }
}
