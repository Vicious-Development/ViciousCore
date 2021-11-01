package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.configuration.Config;
import com.vicious.viciouscore.common.util.configuration.ConfigurationValue;

import java.nio.file.Path;

/**
 * Changes the rendering of a specific part on an entity.
 */
public class ModelRendererConfiguration extends Config {
    public ConfigurationValue<Boolean> active = add(new ConfigurationValue<>("Active", ()->false, this).description("Will the part configuration be applied?").modifyOnRuntime(true));
    //Part Rotation - Controls the Part rotation.
    public ConfigurationValue<Boolean> overrideRotation = add(new ConfigurationValue<>("OverrideRotation", ()->false, this).description("Will the part be rotated?").modifyOnRuntime(true));
    public ConfigurationValue<Float> rx = add(new ConfigurationValue<>("rx", ()->0.0f, this).description("Part's x rotation").modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> ry = add(new ConfigurationValue<>("ry", ()->0.0f, this).description("Part's y rotation").modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> rz = add(new ConfigurationValue<>("rz", ()->0.0f, this).description("Part's z rotation").modifyOnRuntime(true).parent(overrideRotation));

    //Part Translation - Controls the Part position
    public ConfigurationValue<Boolean> overrideTranslation = add(new ConfigurationValue<>("OverrideTranslation", ()->false, this).description("Will the part be translated?").modifyOnRuntime(true));
    public ConfigurationValue<Float> tx = add(new ConfigurationValue<>("tx", ()->0.0f, this).description("Part's x rotation").modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> ty = add(new ConfigurationValue<>("ty", ()->0.0f, this).description("Part's y rotation").modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> tz = add(new ConfigurationValue<>("tz", ()->0.0f, this).description("Part's z rotation").modifyOnRuntime(true).parent(overrideTranslation));


    public ModelRendererConfiguration(Path f) {
        super(f);
    }
}
