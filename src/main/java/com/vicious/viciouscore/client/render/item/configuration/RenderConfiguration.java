package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.configuration.Config;
import com.vicious.viciouscore.common.util.configuration.ConfigurationValue;

import java.nio.file.Path;

/**
 * Saves item transformation data.
 */
public class RenderConfiguration extends Config {
    public ConfigurationValue<Boolean> active = add(new ConfigurationValue<>("Active", ()->false, this).modifyOnRuntime(true));
    //Item Rotation - Controls the item rotation.
    public ConfigurationValue<Boolean> overrideRotation = add(new ConfigurationValue<>("OverrideRotation", ()->false, this).modifyOnRuntime(true));
    public ConfigurationValue<Float> rx = add(new ConfigurationValue<>("rx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> ry = add(new ConfigurationValue<>("ry", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> rz = add(new ConfigurationValue<>("rz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));

    //Item Scale - Controls the item scale.
    public ConfigurationValue<Boolean> overrideScale = add(new ConfigurationValue<>("OverrideScale", ()->false, this).modifyOnRuntime(true));
    public ConfigurationValue<Float> sx = add(new ConfigurationValue<>("sx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));
    public ConfigurationValue<Float> sy = add(new ConfigurationValue<>("sy", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));
    public ConfigurationValue<Float> sz = add(new ConfigurationValue<>("sz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));

    //Item Translation - Controls the item position
    public ConfigurationValue<Boolean> overrideTranslation = add(new ConfigurationValue<>("OverrideTranslation", ()->false, this).modifyOnRuntime(true));
    public ConfigurationValue<Float> tx = add(new ConfigurationValue<>("tx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> ty = add(new ConfigurationValue<>("ty", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> tz = add(new ConfigurationValue<>("tz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));

    public RenderConfiguration(Path f) {
        super(f);
    }
}
