package com.vicious.viciouscore.common.util.tracking.values;

import com.vicious.viciouscore.common.util.VUtil;
import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueConverter;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueJSONParser;
import com.vicious.viciouscore.common.util.tracking.interfaces.TrackableValueStringParser;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TrackableObject<T> extends TrackableValue<T> {
    protected static final Map<Class<?>, TrackableValueJSONParser<?>> jsonparsers = new HashMap<>();
    protected static final Map<Class<?>, TrackableValueStringParser<?>> stringparsers = new HashMap<>();
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
    }
    static {
        stringparsers.put(Boolean.class, Boolean::parseBoolean);
        stringparsers.put(Integer.class, Integer::parseInt);
        stringparsers.put(Double.class, Double::parseDouble);
        stringparsers.put(Float.class, Float::parseFloat);
        stringparsers.put(Byte.class, Byte::parseByte);
        stringparsers.put(Short.class, Short::parseShort);
        stringparsers.put(Long.class, Long::parseLong);
        stringparsers.put(String.class,(j)-> j);
        stringparsers.put(UUID.class, UUID::fromString);
        stringparsers.put(Date.class, VUtil.DATEFORMAT::parse);
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
            if(val != null);
            this.setWithoutUpdate(val);
        } catch(Exception e){
            this.setWithoutUpdate(null);
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> setFromStringWithUpdate(String s) throws Exception{
        try {
            this.set((T) stringparsers.get(type).parse(s));
        } catch(Exception e){
            this.set(null);
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> setFromStringWithoutUpdate(String s) {
        try {
            this.setWithoutUpdate((T) stringparsers.get(type).parse(s));
        } catch(Exception e){
            this.setWithoutUpdate(null);
        }
        this.convert();
        return this;
    }
    public TrackableObject<T> converter(TrackableValueConverter converter){
        return (TrackableObject<T>) super.converter(converter);
    }
}