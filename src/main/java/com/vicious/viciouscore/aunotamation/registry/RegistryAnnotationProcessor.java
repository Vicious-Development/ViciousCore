package com.vicious.viciouscore.aunotamation.registry;

import com.vicious.viciouscore.aunotamation.registry.annotation.Register;
import com.vicious.viciouslib.aunotamation.AnnotationProcessor;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RegistryAnnotationProcessor extends AnnotationProcessor<Register,Class> {
    private final Map<Class<?>,RegistryProcessor<?,?,?>> automators = new HashMap<>();
    public void addSupport(RegistryProcessor<?,?,?> processor){
        processor.setAnnotationProcessor(this);
        automators.put(processor.getTargetType(),processor);
    }
    public RegistryAnnotationProcessor() {
        super(Register.class,Class.class);
    }

    @Override
    public void process(Class registryClass, AnnotatedElement annotatedElement) throws Exception {
        if(annotatedElement instanceof Field f) {
            process(registryClass, f);
        }
        else{
            err(annotatedElement,"must be a field!");
        }
    }
    public void process(Class<?> registryClass, Field f) throws Exception{
        Register r = f.getAnnotation(Register.class);
        AtomicReference<Exception> e = new AtomicReference<>();
        DeepReflection.cycleAndExecute(r.value(),(cls)->{
            if(e.get() != null) return null;
            if(automators.containsKey(cls)){
                try {
                    automators.get(cls).process(registryClass,f);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.set(ex);
                }
            }
            return null;
        });
        Exception ex = e.get();
        if(ex != null) throw ex;
    }
}
