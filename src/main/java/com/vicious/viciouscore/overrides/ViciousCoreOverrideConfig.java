package com.vicious.viciouscore.overrides;

import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouslib.configuration.ConfigurationValue;
import com.vicious.viciouslib.configuration.JSONConfig;

import java.nio.file.Path;

public class ViciousCoreOverrideConfig extends JSONConfig {
    private static ViciousCoreOverrideConfig instance;

    public ViciousCoreOverrideConfig(Path pth) {
        super(pth);
    }

    public static ViciousCoreOverrideConfig getInstance(){
        if(instance == null) instance = new ViciousCoreOverrideConfig(ViciousDirectories.viciousCoreOverrideConfigPath);
        return instance;
    }

    public ConfigurationValue<Boolean> nuclearcraft = add(new ConfigurationValue<>("NuclearcraftOverridesOn", ()->true, this).modifyOnRuntime(false).description("Should nuclearcraft be overridden?"));
    public ConfigurationValue<Boolean> ncfission = add(new ConfigurationValue<>("OptimizeFissionReactors", ()->true, this).modifyOnRuntime(false).description("Should fission reactors be optimized?").parent(nuclearcraft));
    public ConfigurationValue<Boolean> techreborn = add(new ConfigurationValue<>("TechrebornOverridesOn", ()->true, this).modifyOnRuntime(false).description("Should techreborn be overridden?"));
    public ConfigurationValue<Boolean> centrifuge = add(new ConfigurationValue<>("PatchCentrifuge", ()->true, this).modifyOnRuntime(false).description("Should the centrifuge be patched?").parent(techreborn));
    public ConfigurationValue<Boolean> fusion = add(new ConfigurationValue<>("PatchFusionReactor", ()->true, this).modifyOnRuntime(false).description("Should the fusion reactor be patched?").parent(techreborn));
    public ConfigurationValue<Boolean> yabba = add(new ConfigurationValue<>("YABBAOverridesOn", ()->true, this).modifyOnRuntime(false).description("Should YABBA be overridden?"));

}
