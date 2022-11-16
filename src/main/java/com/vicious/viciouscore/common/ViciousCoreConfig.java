package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouslib.persistence.json.JSONFile;
import com.vicious.viciouslib.persistence.storage.PersistentAttribute;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;

public class ViciousCoreConfig extends JSONFile {
    private static final ViciousCoreConfig instance = new ViciousCoreConfig(ViciousDirectories.viciousCoreConfigPath.toString());

    @Save(description = "Do not edit. This is used by the mod to detect if it has ever loaded before.")
    public PersistentAttribute<Boolean> firstLoad = new PersistentAttribute<>("HasLoadedOnce", Boolean.class,false);

    @Save(description = "Enables Debug logging and other things of that sort.")
    public PersistentAttribute<Boolean> debug = new PersistentAttribute<>("DebugSettingsOn", Boolean.class,false);

    @Save(description = "The maximum ticks the client will continue to send button press packets without a response.")
    public PersistentAttribute<Integer> buttonPressResponseTimeOut = new PersistentAttribute<>("ButtonPressResponseTimeOut", Integer.class,100);

    public static ViciousCoreConfig getInstance() {
        return instance;
    }
    public ViciousCoreConfig(String f) {
        super(f);
    }
}
