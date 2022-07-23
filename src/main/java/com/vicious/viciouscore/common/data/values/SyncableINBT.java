package com.vicious.viciouscore.common.data.values;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class SyncableINBT<T extends Tag> extends SyncableValue<T> {
    public SyncableINBT(T value, String name){
        super(value,name);
        this.value=value;
        this.name = name;
        markDirty();
    }
    public T get(){
        return value;
    }
    public void set(T value){
        this.value = value;
        markDirty();
    }

    public void putIntoNBT(CompoundTag tag){
        if(!alwaysDirty && !dirty) return;
        dirty = false;
        tag.put(name,value);
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        value = (T)nbtTagCompound.get(name);
        markDirty();
    }
}
