package com.vicious.viciouscore.common.util.tracking.values;



import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueConverter;
import org.json.JSONObject;

import java.util.function.Supplier;

public class TrackableEnum<T extends Enum<T>> extends TrackableValue<T> {
    public TrackableEnum(String name, Supplier<T> defaultSetting, Trackable<?> tracker) {
        super(name, defaultSetting, tracker);
    }
    public TrackableEnum<T> setFromJSON(JSONObject jo) throws Exception{
        try {
            this.setWithoutUpdate(jo.getEnum(type,name));
        } catch(Exception e){
            throw e;
        }
        this.convert();
        return this;
    }
    public TrackableEnum<T> setFromStringWithUpdate(String s) throws Exception{
        try {
            this.set(Enum.valueOf(type,s));
        } catch(Exception e){
        }
        this.convert();
        return this;
    }
    public TrackableEnum<T> setFromStringWithoutUpdate(String s) {
        try {
            this.setWithoutUpdate(Enum.valueOf(type,s));
        } catch(Exception e){
        }
        this.convert();
        return this;
    }
    public TrackableEnum<T> set(T setting){
        return (TrackableEnum<T>) super.set(setting);
    }

    public TrackableEnum<T> setWithoutUpdate(T setting){
        return (TrackableEnum<T>) super.setWithoutUpdate(setting);
    }
    public TrackableEnum<T> converter(TrackableValueConverter converter){
        return (TrackableEnum<T>) super.converter(converter);
    }
}