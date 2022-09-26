package com.vicious.viciouscore.common.data.autogen;

import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityProviderHolder;
import com.vicious.viciouscore.common.data.autogen.annotation.*;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.ExposableSyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouslib.util.ClassAnalyzer;
import com.vicious.viciouslib.util.reflect.ClassManifest;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class handles fields Annotated with @Exposed.
 * SyncableValues labelled with such will automatically be added to a ExposableSyncableCompound object should they be initialized correctly.
 */
public class SyncAutomator {
    /**
     * Checks if the class can be properly automated.
     * These checks must be passed:
     * Any fields annotated with @Sync or @Exposed or @Obscured must be public, non static, and initialized without being added to the SyncableCompound.
     * If any field is improperly labelled a FailedAutomationException will be thrown and will crash the program.
     */
    private static void readyForSetup(Class<?> cls){
        if(!ClassAnalyzer.manifests.containsKey(cls)) {
            ClassAnalyzer.analyzeClass(cls);
        }
    }
    private static void checkValid(Field f, Class<?> annotationClass){
        if (Modifier.isPrivate(f.getModifiers())) {
            throw new FailedAutomationException(f.getName() + " in " + f.getDeclaringClass().getCanonicalName() + " annotated with " + annotationClass.getName() + " must be declared public!");
        } else if (Modifier.isStatic(f.getModifiers())) {
            throw new FailedAutomationException(f.getName() + " in " + f.getDeclaringClass().getCanonicalName() + " annotated with " + annotationClass.getName() + " cannot be static!");
        } else if(!SyncableValue.class.isAssignableFrom(f.getType())) {
            throw new FailedAutomationException(f.getName() + " in " + f.getDeclaringClass().getCanonicalName() + " annotated with " + annotationClass.getName() + " must have type extending SyncableValue!");
        }
    }

    /**
     * Processes all annotations in a SyncableComoundHolder.
     */
    public static void automateInitialization(ISyncableCompoundHolder holder){
        setup(holder);
    }

    private static void setup(ISyncableCompoundHolder compound) {
        readyForSetup(compound.getClass());
        Class<?> cls = compound.getClass();
        while(cls != null){
            setup(compound,cls);
            //Some compounds extend off each other, we should check the super manifests as well before finishing.
            //We ignore interfaces since they can't have member fields.
            cls = cls.getSuperclass();
        }
    }
    private static void setup(ISyncableCompoundHolder compound, Class<?> cls) {
        ClassManifest<?> manif = ClassAnalyzer.manifests.get(cls);
        if(manif != null) {
            process(manif,Exposed.class,(anno)->{
                if(anno instanceof Field f){
                    checkValid(f,Exposed.class);
                    if (compound.getData().containsKey(f.getName())) {
                        throw new FailedAutomationException(f.getName() + " in " + cls + " annotated with Exposed must not already be added!");
                    } else {
                        if(compound instanceof IVCCapabilityProviderHolder prov) {
                            ExposableSyncableCompound exposer = new ExposableSyncableCompound(f.getName(), prov.getProvider());
                            ensureInCompound(exposer,f,Exposed.class);
                            compound.getData().add(exposer);
                        }
                        else{
                            throw new FailedAutomationException(f.getName() + " in " + cls + " annotated with Exposed must be in a class implementing IVCCapabilityProviderHolder!");
                        }
                    }
                }
            });
            process(manif,Persist.class,(anno)->{
                if(anno instanceof Field f){
                    checkValid(f,Persist.class);
                    ensureInCompound(compound,f,Persist.class);
                }
            });
            process(manif,Editable.class,(anno)->{
                if(anno instanceof Field f){
                    checkValid(f,Editable.class);
                    ensureInCompound(compound,f,Editable.class).sendRemote(true).readRemote(true);
                }
            });
            process(manif,ReadOnly.class,(anno)->{
                if(anno instanceof Field f){
                    checkValid(f,ReadOnly.class);
                    ensureInCompound(compound,f,ReadOnly.class).sendRemote(true).readRemote(false);
                }
            });
            process(manif,Obscured.class,(anno)->{
                if(anno instanceof Field f){
                    checkValid(f,Obscured.class);
                    ensureInCompound(compound,f,Obscured.class).sendRemote(false).readRemote(false);
                }
            });
        }
    }
    private static SyncableValue<?> ensureInCompound(ISyncableCompoundHolder compound, Field f, Class<?> annotation){
        try {
            SyncableValue<?> o = (SyncableValue<?>) requireInitialized(f, compound, annotation);
            if (!compound.getData().containsKey(o.KEY)) {
                compound.getData().add((SyncableValue<?>) o);
            }
            return o;
        }
        catch (IllegalAccessException e) {
            throw new FailedAutomationException(f.getName() + " in " + f.getDeclaringClass() + " annotated with " + annotation.getName() + " must be declared public!");
        }
    }
    private static void process(ClassManifest<?> manif, Class<? extends Annotation> annotation, Consumer<AnnotatedElement> cons){
        List<AnnotatedElement> annos = manif.getMembersWithAnnotation(annotation);
        if(annos != null){
            for (AnnotatedElement anno : annos) {
                cons.accept(anno);
            }
        }
    }
    private static Object requireInitialized(Field f, Object src, Class<?> annotation) throws IllegalAccessException {
        Object o = f.get(src);
        if (o == null) throw new FailedAutomationException(f.getName() + " in " + f.getDeclaringClass() + " annotated with "+ annotation.getName() +" must be initialized on declaration!");
        return o;
    }
}
