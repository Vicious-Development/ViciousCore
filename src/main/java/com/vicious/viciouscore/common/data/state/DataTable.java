package com.vicious.viciouscore.common.data.state;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A memory based database mainly provided so that I can use multiple maps to store the same thing.
 */
public class DataTable<T> implements IDataTable<T>{
    private final Map<Class<?>, Map<Object,T>> datamap = new HashMap<>();
    private final Map<Class<?>, Function<T,Object>> keyfunctions = new LinkedHashMap<>();
    public DataTable(){
        //This uses the object as a key for itself. We use this mostly for storing one map guaranteed to have every object.
        supports(o->o,Object.class);
    }

    /**
     * Note that this adds the function to the front of the LinkedHashMap.
     * Faster keys should be added last in code.
     */
    public DataTable<T> supports(Function<T, Object> keyfunc, Class<?> keyClass){
        Set<Map.Entry<Class<?>, Function<T,Object>>> set = new HashSet<>(keyfunctions.entrySet());
        keyfunctions.clear();
        keyfunctions.put(keyClass,keyfunc);
        for (Map.Entry<Class<?>, Function<T, Object>> classFunctionEntry : set) {
            keyfunctions.put(classFunctionEntry.getKey(),classFunctionEntry.getValue());
        }
        return this;
    }
    public void add(T t){
        for (Class<?> q : keyfunctions.keySet()) {
            Map<Object,T> map = getDataMap(q);
            Object key = keyfunctions.get(q).apply(t);
            if(key != null) map.put(key,t);
        }
    }

    /**
     * Remove the object to all maps using keyfunctions.
     * @param t - The value.
     */
    public void remove(T t){
        for (Class<?> q : keyfunctions.keySet()) {
            Map<Object,T> map = getDataMap(q);
            Object key = keyfunctions.get(q).apply(t);
            if(key != null) map.remove(key);
        }
    }
    public boolean containsValue(T t){
        for (Class<?> q : keyfunctions.keySet()) {
            Map<Object,T> map = getDataMap(q);
            Object key = keyfunctions.get(q).apply(t);
            if(key != null) return map.get(key) != null;
        }
        return false;
    }
    public boolean containsKey(Object o){
        return get(o) != null;
    }


    /**
     * Gets the fastest map for the provided key class.
     * @param o - The key
     * @return - The value
     */
    public T get(Object o){
        if(o == null) return null;
        return getDataMap(o.getClass()).get(o);
    }
    private Map<Object,T> getDataMap(Class<?> cls){
        Map<Object,T> datMap = datamap.get(cls);
        if(datMap == null){
            datMap = new HashMap<>();
            datamap.put(cls,datMap);
        }
        return datMap;
    }

    public void forEach(Consumer<T> consumer) {
        for (T value : getDataMap(Object.class).values()) {
            consumer.accept(value);
        }
    }

    @Override
    public int size() {
        return getDataMap(Object.class).size();
    }

    @Override
    public String toString() {
        return "DataTable{" +
                "datamap=" + datamap +
                ", keyfunctions=" + keyfunctions +
                '}';
    }
}
