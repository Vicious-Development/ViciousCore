package com.vicious.viciouscore.common;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.configuration.Config;

import java.nio.file.Path;

public class VCoreConfig extends Config {
    public static VCoreConfig init(){
        //If the old CFG still exists, this will probably send an error which is totally fine. Instantiation will not fail. Task Failed Successfully.
        VCoreConfig cfg = new VCoreConfig(Directories.viciousCoreConfigPath);
        temp.put(Directories.viciousCoreConfigPath,cfg);
        //INSTANCE.overWriteFile();
        return cfg;
    }
    public VCoreConfig(Path f) {
        super(f);
    }

}
