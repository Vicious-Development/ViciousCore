package com.vicious.viciouscore.common.util.configuration;


import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.values.TrackableObject;

import java.util.function.Supplier;

public class ConfigurationValue<T> extends TrackableObject<T> {
    public String description = "";
    private T settingOnStop;
    private boolean canBeSetOnRuntime = true;
    private ConfigurationValue parent;
    public ConfigurationValue(String name, Supplier<T> defaultSetting, Trackable<?> tracker){
        super(name,defaultSetting,tracker);
        settingOnStop = defaultSetting.get();
    }

    public ConfigurationValue<T> set(T setting) {
        if(canBeSetOnRuntime){
            settingOnStop=setting;
            return (ConfigurationValue<T>) super.set(setting);
        }
        else {
            settingOnStop=setting;
            return this;
        }
    }

    //Returns the false if the parent is false.
    public boolean getBoolean(){
        if(parent == null && setting instanceof Boolean) return (Boolean) setting;
        if(parent.setting instanceof Boolean && setting instanceof Boolean) return (Boolean)parent.setting && (Boolean) setting;
        else return true;
    }
    public ConfigurationValue<T> modifyOnRuntime(boolean val){
        canBeSetOnRuntime=val;
        return this;
    }
    public boolean canBeModifiedOnRuntime(){
        return canBeSetOnRuntime;
    }
    public T getStopValue(){
        return settingOnStop;
    }

    public ConfigurationValue<T> parent(ConfigurationValue parent) {
        this.parent=parent;
        return this;
    }
    public ConfigurationValue<T> description(String desc){
        this.description=desc;
        return this;
    }
    public boolean hasParent(){
        return parent != null;
    }

    public boolean isBoolean() {
        return setting instanceof Boolean;
    }

    @Override
    public ConfigurationValue<T> setWithoutUpdate(T setting){
        settingOnStop = setting;
        return (ConfigurationValue<T>) super.setWithoutUpdate(setting);
    }

    public String getTab() {
        if(parent == null) return "";
        return parent.getTab() + "  ";
    }
}
