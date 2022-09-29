package com.vicious.viciouscore.aunotamation.isyncablecompoundholder;

import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.*;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.ExposableSyncableCompound;
import com.vicious.viciouslib.aunotamation.Aunotamation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

/**
 * This class handles fields Annotated with @Exposed.
 * SyncableValues labelled with such will automatically be added to a ExposableSyncableCompound object should they be initialized correctly.
 */
public class SyncAutomator {
    private static boolean init = false;
    public static void init(){
        if(init) return;
        init=true;
        Aunotamation.registerProcessor(new SyncProcessor<>(Persist.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno) {
                if(anno instanceof Field f){
                    ensureInCompound(compound,f);
                }
            }
        });
        Aunotamation.registerProcessor(new SyncProcessor<>(Obscured.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno) throws IllegalAccessException {
                requireInitialized(compound,anno,(sv)-> sv.sendRemote(false).readRemote(false));
            }
        });
        Aunotamation.registerProcessor(new SyncProcessor<>(ReadOnly.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno) throws IllegalAccessException {
                requireInitialized(compound,anno,(sv)-> sv.sendRemote(true).readRemote(false));
            }
        });
        Aunotamation.registerProcessor(new SyncProcessor<>(Editable.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno) throws IllegalAccessException {
                requireInitialized(compound,anno,(sv)-> sv.sendRemote(true).readRemote(true));
            }
        });
        Aunotamation.registerProcessor(new SyncProcessor<>(Exposed.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno) {
                if(anno instanceof Field f){
                    //Remove if already added.
                    compound.getData().remove(f.getName());
                    ExposableSyncableCompound exposer = new ExposableSyncableCompound(f.getName());
                    ensureInCompound(exposer,f);
                    compound.getData().add(exposer);
                }
            }
        });
    }
    /**
     * Processes all annotations in a SyncableCompoundHolder.
     */
    public static void automateInitialization(ISyncableCompoundHolder holder){
        Aunotamation.processObject(holder);
    }
}
