package com.vicious.viciouscore.aunotamation;

import com.vicious.viciouscore.aunotamation.commonkeybinding.CommonKeyBindingAutomator;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.SyncAutomator;
import com.vicious.viciouscore.aunotamation.registry.RegistryAutomator;

public class Aunotamations {
    public static void init(){
        SyncAutomator.init();
        RegistryAutomator.init();
        CommonKeyBindingAutomator.init();
    }
}
