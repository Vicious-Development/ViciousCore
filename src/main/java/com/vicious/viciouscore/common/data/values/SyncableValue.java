package com.vicious.viciouscore.common.data.values;

public abstract class SyncableValue<T> extends Syncable<T>{
    protected T value;
    public String name;
    public SyncableValue(T value, String name) {
        this.value = value;
        this.name = name;
    }
    public T get(){
        return value;
    }
}
