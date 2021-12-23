package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.util.file.Directories;
import com.vicious.viciouslib.configuration.ConfigurationValue;
import com.vicious.viciouslib.configuration.JSONConfig;

import java.nio.file.Path;

public class VCoreConfig extends JSONConfig {
    private static VCoreConfig instance;
    public ConfigurationValue<Boolean> firstLoad = add(new ConfigurationValue<>("FirstLoadDone", ()->false, this).modifyOnRuntime(true).description("Whether the mod has loaded one time. Do not change unless you want a lot of things to reset."));
    public ConfigurationValue<Boolean> debug = add(new ConfigurationValue<>("DebugSettingsOn", ()->false, this).modifyOnRuntime(true).description("Enable Debug?"));
    public static VCoreConfig getInstance(){
        if(instance == null) instance = new VCoreConfig(Directories.viciousCoreConfigPath);
        return instance;
    }
    public VCoreConfig(Path f) {
        super(f);
    }
}
