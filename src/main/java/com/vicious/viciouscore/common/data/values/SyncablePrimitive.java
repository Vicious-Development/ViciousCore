package com.vicious.viciouscore.common.data.values;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class SyncablePrimitive<T> extends SyncableValue<T> {
    public SyncablePrimitive(T value, String name){
        super(value,name);
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
        if(value instanceof Integer v){
            tag.putInt(name, v);
        }
        else if(value instanceof Double v){
            tag.putDouble(name, v);
        }
        else if(value instanceof Boolean v){
            tag.putBoolean(name, v);
        }
        else if(value instanceof Float v){
            tag.putFloat(name, v);
        }
        else if(value instanceof Byte v){
            tag.putByte(name, v);
        }
        else if(value instanceof Long v){
            tag.putLong(name, v);
        }
        else if(value instanceof Short v){
            tag.putShort(name, v);
        }
        else if(value instanceof UUID v){
            tag.putUUID(name, v);
        }
        else if(value instanceof String v){
            tag.putString(name, v);
        }
        else if(value instanceof IntList v){
            tag.putIntArray(name,v.toArray(new int[0]));
        }
    }
    @SuppressWarnings("unchecked")
    public void readFromNBT(CompoundTag tag){
        if(value instanceof Integer){
            value = (T)(Integer)tag.getInt(name);
        }
        else if(value instanceof Double){
            value = (T)(Double)tag.getDouble(name);
        }
        else if(value instanceof Boolean){
            value = (T)(Boolean)tag.getBoolean(name);
        }
        else if(value instanceof Float){
            value = (T)(Float)tag.getFloat(name);
        }
        else if(value instanceof Byte){
            value = (T)(Byte)tag.getByte(name);
        }
        else if(value instanceof Long){
            value = (T)(Long)tag.getLong(name);
        }
        else if(value instanceof Short){
            value = (T)(Short)tag.getShort(name);
        }
        else if(value instanceof UUID){
            value = (T)tag.getUUID(name);
        }
        else if(value instanceof String){
            value = (T)tag.getString(name);
        }
        else if(value instanceof IntList){
            value = (T) new IntArrayList(tag.getIntArray(name));
        }
        markDirty();
    }
}
