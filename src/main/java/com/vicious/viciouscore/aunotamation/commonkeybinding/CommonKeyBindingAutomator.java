package com.vicious.viciouscore.aunotamation.commonkeybinding;

import com.vicious.viciouscore.aunotamation.VCoreProcessor;
import com.vicious.viciouscore.common.keybinding.CommonKeyBinding;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouslib.aunotamation.Aunotamation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

public class CommonKeyBindingAutomator {
    public static void init() {
        Aunotamation.registerProcessor(new VCoreProcessor<>(Key.class, Object.class) {
            @Override
            public void process(Object obj, AnnotatedElement anno) throws IllegalAccessException {
                if(anno instanceof Field f){
                    CommonKeyBinding ckb = (CommonKeyBinding) requireInitialized(f,obj);
                    CommonKeyBindings.add(ckb);
                }
            }
        });
        Aunotamation.registerProcessor(new VCoreProcessor<>(Mouse.class, Object.class) {
            @Override
            public void process(Object obj, AnnotatedElement anno) throws IllegalAccessException {
                if(anno instanceof Field f){
                    CommonKeyBinding ckb = (CommonKeyBinding) requireInitialized(f,obj);
                    ckb.isMouse(true);
                    CommonKeyBindings.add(ckb);
                }
            }
        });
        CommonKeyBindings.setup();
    }
}
