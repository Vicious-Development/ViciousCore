package com.vicious.viciouscore.common.util.tracking.values;

import com.vicious.viciouscore.common.util.VUtil;
import com.vicious.viciouscore.common.util.tracking.serialization.SerializableArray;
import com.vicious.viciouscore.common.util.tracking.serialization.SerializationUtil;
import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueConverter;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueJSONParser;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TrackableObject<T> extends TrackableValue<T> {
    protected static final Map<Class<?>, TrackableValueJSONParser<?>> jsonparsers = new HashMap<>();

    static {
        jsonparsers.put(Boolean.class,(j, t)->j.getBoolean(t.name));
        jsonparsers.put(Integer.class,(j, t)->j.getInt(t.name));
        jsonparsers.put(Double.class,(j, t)->j.getDouble(t.name));
        jsonparsers.put(Float.class,(j, t)->j.getFloat(t.name));
        //Idk if this works so if it errors out. Blame the Thon.
        jsonparsers.put(Byte.class,(j, t)->j.getInt(t.name));
        jsonparsers.put(Short.class,(j, t)->j.getInt(t.name));
        jsonparsers.put(Long.class,(j, t)->j.getLong(t.name));
        jsonparsers.put(String.class,(j, t)->j.getString(t.name));
        jsonparsers.put(UUID.class,(j, t)->UUID.fromString(j.getString(t.name)));
        jsonparsers.put(Date.class,(j, t)-> VUtil.DATEFORMAT.parse(j.getString(t.name)));
        jsonparsers.put(SerializableArray.class,(j, t)-> ((TrackableArrayValue<?>)t).setting.parse(j.getString(t.name)));
    }

    public TrackableObject(String name, Supplier<T> defaultSetting, Trackable<?> tracker){
        super(name, defaultSetting, tracker);

    }

    public TrackableObject(String name, Supplier<T> defaultSetting, Trackable<?> tracker, Class<T> type) {
        super(name, defaultSetting, tracker,type);
    }

    public TrackableObject<T> set(T setting){
        return (TrackableObject<T>) super.set(setting);
    }

    public TrackableObject<T> setWithoutUpdate(T setting){
        return (TrackableObject<T>) super.setWithoutUpdate(setting);
    }

    public TrackableObject<T> setFromJSON(JSONObject jo) {
        try {
            T val = ((TrackableValueJSONParser<T>) jsonparsers.get(type)).parse(jo, this);
            this.setWithoutUpdate(val);
        } catch(Exception ignored){
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> setFromStringWithUpdate(String s) throws Exception{
        try {
            this.set((T) SerializationUtil.stringparsers.get(type).parse(s));
        } catch(Exception e){
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> setFromStringWithoutUpdate(String s) {
        try {
            this.setWithoutUpdate((T) SerializationUtil.stringparsers.get(type).parse(s));
        } catch(Exception e){
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> converter(TrackableValueConverter converter){
        return (TrackableObject<T>) super.converter(converter);
    }
}