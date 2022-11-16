package com.vicious.viciouscore.aunotamation.isyncablecompoundholder;

import com.vicious.viciouscore.aunotamation.ForcedExposure;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.*;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.ExposableSyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouslib.aunotamation.Aunotamation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

/**
 * This class handles fields Annotated with @Persist, @Editable, @ReadOnly, @Obscured, and @Exposed.
 */
public class SyncAutomator {
    private static boolean init = false;
    public static void init(){
        if(init) return;
        init=true;
        //Processes internal components.
        Aunotamation.registerObjectProcessor(ISyncableCompoundHolder.class,(o)->{
            if(o instanceof ISyncableCompoundHolder sch){
                SyncableCompound c2 = sch.getData();
                c2.forEachSyncable((sv)->{
                    if(c2 != sv) {
                        Aunotamation.processObject(sv);
                    }
                });
            }
        });
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
        Aunotamation.registerProcessor(new SyncProcessor<>(Controlled.class,ISyncableCompoundHolder.class) {
            @Override
            public void process(ISyncableCompoundHolder compound, AnnotatedElement anno){}
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
                    Exposed exposed = anno.getAnnotation(Exposed.class);
                    //Remove if already added.
                    compound.getData().remove(f.getName());
                    ExposableSyncableCompound exposer = new ExposableSyncableCompound(f.getName());
                    try {
                        exposer.add((SyncableValue<?>)f.get(compound));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    compound.getData().add(exposer);
                    for (String s : exposed.value()) {
                        exposer.expose(ForcedExposure.getExposureOfType(s));
                    }
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
