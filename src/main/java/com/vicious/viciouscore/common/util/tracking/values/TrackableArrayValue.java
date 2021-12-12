package com.vicious.viciouscore.common.util.tracking.values;

import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.serialization.SerializableArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TrackableArrayValue<T> extends TrackableObject<SerializableArray<T>> {
    public TrackableArrayValue(String name, Class<T> type, Trackable<?> tracker, Integer... defaultArrayDimensions) {
        super(name, () -> new SerializableArray<T>(type, defaultArrayDimensions), tracker);
    }

    @Override
    public TrackableArrayValue<T> setFromJSON(JSONObject jo) {
        try {
            SerializableArray<T> val = (SerializableArray<T>) jsonparsers.get(type).parse(jo, this);
            this.setWithoutUpdate(val);
        } catch (Exception ignored) {
        }
        this.convert();
        return this;
    }

    public T get(Integer... pos) {
        //Failure to cast means that the pos is either invalid the type stored is not correct.
        return (T) setting.deepGet(pos);
    }

    public T remove(Integer... pos) {
        //Failure to cast means that the pos is either invalid the type stored is not correct.
        T pre = (T) setting.deepGet(pos);
        setting.deepSet(null, pos);
        tracker.markDirty(name, setting);
        return pre;
    }

    //Has the potential to be lossy. Do not shrink unless intentional.
    public void resize(Integer... size) {
        setting.deepResize(size);
    }

    public TrackableArrayValue<T> set(T val, Integer... pos) {
        setting.deepSet(val, pos);
        tracker.markDirty(name, setting);
        return this;
    }

    /**
     * LIST FUNCTIONS
     * WARNING!!!
     * List methods may not function as expected in Multidimensional arrays. For this reason, remain 1 dimensional while using list functions.
     */
    public void addAll(Collection<T> vals) {
        for (T val : vals) {
            setting.add(val);
        }
        tracker.markDirty(name,setting);
    }

    public void clear() {
        setting = new SerializableArray<T>(setting.type,setting.dimensions);
    }

    public void forEach(Consumer<T> consumer) {
        if(setting == null) return;
        setting.forEach(consumer);
    }

    public void forEachBreak(BiConsumer<AtomicBoolean,T> consumer) {
        if(setting == null) return;
        setting.forEachBreak(consumer);
    }

    public boolean contains(T val){
        return setting.contains(val);
    }

    public int indexOf(T val){
        return setting.indexOf(val);
    }

    public void add(T state) {
        setting.add(state);
        tracker.markDirty(name,setting);
    }

    public int size() {
        return setting.length();
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> list = new ArrayList<>();
        setting.forEach(list::add);
        return list;
    }
}
