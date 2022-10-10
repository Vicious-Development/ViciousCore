package com.vicious.viciouscore.aunotamation.isyncablecompoundholder;

import com.vicious.viciouscore.aunotamation.VCoreProcessor;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public abstract class SyncProcessor<A extends Annotation> extends VCoreProcessor<A, ISyncableCompoundHolder> {
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
    protected void requireInitialized(ISyncableCompoundHolder compound, AnnotatedElement anno, Consumer<SyncableValue<?>> cons) throws IllegalAccessException {
        if(anno instanceof Field f){
            if(requireInitialized(f, compound) instanceof SyncableValue<?> sv) {
                cons.accept(sv);
            }
        }
    }
}
