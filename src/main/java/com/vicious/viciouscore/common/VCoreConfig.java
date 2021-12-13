package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.util.file.Directories;
import com.vicious.viciouslib.configuration.ConfigurationValue;
import com.vicious.viciouslib.configuration.JSONConfig;

import java.nio.file.Path;

public class VCoreConfig extends JSONConfig {
    public ConfigurationValue<Boolean> firstLoad = add(new ConfigurationValue<>("FirstLoadDone", ()->false, this).modifyOnRuntime(true).description("Whether the mod has loaded one time. Do not change unless you want a lot of things to reset."));
    public ConfigurationValue<Boolean> debug = add(new ConfigurationValue<>("DebugSettingsOn", ()->false, this).modifyOnRuntime(true).description("Enable Debug?"));
    public static VCoreConfig init(){
        //If the old CFG still exists, this will probably send an error which is totally fine. Instantiation will not fail. Task Failed Successfully.
        VCoreConfig cfg = new VCoreConfig(Directories.viciousCoreConfigPath);
        temp.put(Directories.viciousCoreConfigPath,cfg);
        return cfg;
    }
    public VCoreConfig(Path f) {
        super(f);
    }
}
