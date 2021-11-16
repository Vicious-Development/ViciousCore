package com.vicious.viciouscore.common.util.tracking.values;

import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.TrackingHandler;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueConverter;
import org.json.JSONObject;

import java.util.function.Supplier;

public abstract class TrackableValue<T> {
    public static TrackingHandler globalHandler;

    public final T defaultSetting;
    protected T setting;
    public final Class<T> type;
    public final String name;
    public Trackable<?> tracker;
    protected TrackableValueConverter converter;

    protected TrackableValue(String name, Supplier<T> defaultSetting, Trackable<?> tracker){
        this.name=name;
        this.defaultSetting=defaultSetting.get();
        this.type= (Class<T>) this.defaultSetting.getClass();
        this.setting=defaultSetting.get();
        this.tracker=tracker;
    }
    public TrackableValue(String name, Supplier<T> defaultSetting, Trackable<?> tracker, Class<T> type) {
        this.name=name;
        this.defaultSetting=defaultSetting.get();
        this.type= type;
        this.setting=defaultSetting.get();
        this.tracker=tracker;
    }

    public static <V> TrackableValue<V> fromString(String substring, Class<V> type) {
        TrackableObject<V> obj = new TrackableObject<>("",()->null,null,type);
        obj.setFromStringWithoutUpdate(substring);
        return obj.value() == null ? null : obj;
    }

    public T value() {
        return setting;
    }

    public void convert(){
        if(converter != null && this.value() != null) converter.convert(this);
    }
    //Used to convert a trackable's value into something else.
    public TrackableValue<T> converter(TrackableValueConverter converter){
        this.converter=converter;
        return this;
    }
    public TrackableValue<T> set(T setting){
        //Failure is expected only if the dev screwed up.
        this.setting=setting;
        //Mark dirty.
        tracker.markDirty(name,setting);
        return this;
    }

    public TrackableValue<T> setWithoutUpdate(T setting){
        //Failure is expected only if the dev screwed up.
        this.setting=setting;
        return this;
    }
    public abstract TrackableValue<T> setFromJSON(JSONObject jo) throws Exception;
    public abstract TrackableValue<T> setFromStringWithUpdate(String s) throws Exception;
    public abstract TrackableValue<T> setFromStringWithoutUpdate(String s) throws Exception;
    public String toString(){
        return setting.toString();
    }

    public TrackableValue<T> setUnchecked(Object setting) {
        //Failure is expected only if the dev screwed up.
        this.setting=(T)setting;
        //Mark dirty.
        return this;
    }
}
