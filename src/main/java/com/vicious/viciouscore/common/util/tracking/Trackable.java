package com.vicious.viciouscore.common.util.tracking;

import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Trackable<T extends Trackable<T>> {
    protected static TrackingHandler handler;
    public final Map<String, TrackableValue<?>> values = new LinkedHashMap<>();
    public void insertJSONObject(){

    }
    public <E extends TrackableValue<?>> E add(E t){
        values.put(t.name,t);
        return t;
    }
    public abstract void markDirty(String variablename, Object var);
    public static void setHandler(TrackingHandler instance) {
        handler=instance;
    }
    public T updateValue(String name, Object value) throws Exception {
        try {
            values.get(name).setUnchecked(value);
        } catch(Exception e){
            throw new Exception("Value not found, value names are cAsE SEnSITive!");
        }
        return (T)this;
    }
    public Object getValue(String name){
        return values.get(name).value();
    }

    public void forEachValue(Predicate<TrackableValue<?>> predicate, Consumer<TrackableValue<?>> run){
        for (TrackableValue<?> value : values.values()) {
            try {
                if (predicate.test(value)) {
                    run.accept(value);
                }
            } catch(Exception ignored){}
        }
    }

}