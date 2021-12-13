package com.vicious.viciouscore.client.configuration;

import com.vicious.viciouslib.configuration.ConfigurationValue;
import com.vicious.viciouslib.configuration.JSONConfig;

import java.nio.file.Path;

/**
 * Saves item transformation data.
 */
public class ItemTransformOverrideCFG extends JSONConfig {
    public ConfigurationValue<Boolean> active = add(new ConfigurationValue<>("Active", ()->false, this).modifyOnRuntime(true));
    //Item Rotation - Controls the item rotation.
    public ConfigurationValue<Boolean> overrideRotation = add(new ConfigurationValue<>("OverrideRotation", ()->false, this).modifyOnRuntime(true).parent(active));
    public ConfigurationValue<Float> rx = add(new ConfigurationValue<>("rx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> ry = add(new ConfigurationValue<>("ry", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));
    public ConfigurationValue<Float> rz = add(new ConfigurationValue<>("rz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideRotation));

    //Item Scale - Controls the item scale.
    public ConfigurationValue<Boolean> overrideScale = add(new ConfigurationValue<>("OverrideScale", ()->false, this).modifyOnRuntime(true).parent(active));
    public ConfigurationValue<Float> sx = add(new ConfigurationValue<>("sx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));
    public ConfigurationValue<Float> sy = add(new ConfigurationValue<>("sy", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));
    public ConfigurationValue<Float> sz = add(new ConfigurationValue<>("sz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideScale));

    //Item Translation - Controls the item position
    public ConfigurationValue<Boolean> overrideTranslation = add(new ConfigurationValue<>("OverrideTranslation", ()->false, this).modifyOnRuntime(true).parent(active));
    public ConfigurationValue<Float> tx = add(new ConfigurationValue<>("tx", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> ty = add(new ConfigurationValue<>("ty", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));
    public ConfigurationValue<Float> tz = add(new ConfigurationValue<>("tz", ()->0.0f, this).modifyOnRuntime(true).parent(overrideTranslation));

    public ConfigurationValue<Boolean> guiactive = add(new ConfigurationValue<>("GUIActive", ()->false, this).modifyOnRuntime(true));
    //GUI Item Rotation - Controls the item rotation.
    public ConfigurationValue<Boolean> guioverrideRotation = add(new ConfigurationValue<>("GUIOverrideRotation", ()->false, this).modifyOnRuntime(true).parent(guiactive));
    public ConfigurationValue<Float> grx = add(new ConfigurationValue<>("grx", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideRotation));
    public ConfigurationValue<Float> gry = add(new ConfigurationValue<>("gry", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideRotation));
    public ConfigurationValue<Float> grz = add(new ConfigurationValue<>("grz", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideRotation));

    //GUI Item Scale - Controls the item scale.
    public ConfigurationValue<Boolean> guioverrideScale = add(new ConfigurationValue<>("GUIOverrideScale", ()->false, this).modifyOnRuntime(true).parent(guiactive));
    public ConfigurationValue<Float> gsx = add(new ConfigurationValue<>("gsx", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideScale));
    public ConfigurationValue<Float> gsy = add(new ConfigurationValue<>("gsy", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideScale));
    public ConfigurationValue<Float> gsz = add(new ConfigurationValue<>("gsz", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideScale));

    //GUI Item Translation - Controls the item position
    public ConfigurationValue<Boolean> guioverrideTranslation = add(new ConfigurationValue<>("GUIOverrideTranslation", ()->false, this).modifyOnRuntime(true).parent(guiactive));
    public ConfigurationValue<Float> gtx = add(new ConfigurationValue<>("gtx", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideTranslation));
    public ConfigurationValue<Float> gty = add(new ConfigurationValue<>("gty", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideTranslation));
    public ConfigurationValue<Float> gtz = add(new ConfigurationValue<>("gtz", ()->0.0f, this).modifyOnRuntime(true).parent(guioverrideTranslation));

    public ConfigurationValue<Boolean> fpactive = add(new ConfigurationValue<>("FPActive", ()->false, this).modifyOnRuntime(true));
    //First person Item Rotation - Controls the item rotation.
    public ConfigurationValue<Boolean> fpoverrideRotation = add(new ConfigurationValue<>("FPOverrideRotation", ()->false, this).modifyOnRuntime(true).parent(fpactive));
    public ConfigurationValue<Float> frx = add(new ConfigurationValue<>("frx", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideRotation));
    public ConfigurationValue<Float> fry = add(new ConfigurationValue<>("fry", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideRotation));
    public ConfigurationValue<Float> frz = add(new ConfigurationValue<>("frz", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideRotation));

    //First person Item Scale - Controls the item scale.
    public ConfigurationValue<Boolean> fpoverrideScale = add(new ConfigurationValue<>("FPOverrideScale", ()->false, this).modifyOnRuntime(true).parent(fpactive));
    public ConfigurationValue<Float> fsx = add(new ConfigurationValue<>("fsx", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideScale));
    public ConfigurationValue<Float> fsy = add(new ConfigurationValue<>("fsy", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideScale));
    public ConfigurationValue<Float> fsz = add(new ConfigurationValue<>("fsz", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideScale));

    //First person Item Translation - Controls the item position
    public ConfigurationValue<Boolean> fpoverrideTranslation = add(new ConfigurationValue<>("FPOverrideTranslation", ()->false, this).modifyOnRuntime(true).parent(fpactive));
    public ConfigurationValue<Float> ftx = add(new ConfigurationValue<>("ftx", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideTranslation));
    public ConfigurationValue<Float> fty = add(new ConfigurationValue<>("fty", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideTranslation));
    public ConfigurationValue<Float> ftz = add(new ConfigurationValue<>("ftz", ()->0.0f, this).modifyOnRuntime(true).parent(fpoverrideTranslation));


    public ItemTransformOverrideCFG(Path f) {
        super(f);
    }
}
