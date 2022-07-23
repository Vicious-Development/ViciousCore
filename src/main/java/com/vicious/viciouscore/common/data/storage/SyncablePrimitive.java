package com.vicious.viciouscore.common.data.storage;

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
        if(value instanceof Integer){
            tag.putInt(name, (Integer)value);
        }
        else if(value instanceof Double){
            tag.putDouble(name, (Double)value);
        }
        else if(value instanceof Boolean){
            tag.putBoolean(name, (Boolean)value);
        }
        else if(value instanceof Float){
            tag.putFloat(name, (Float)value);
        }
        else if(value instanceof Byte){
            tag.putByte(name, (Byte)value);
        }
        else if(value instanceof Long){
            tag.putLong(name, (Long)value);
        }
        else if(value instanceof Short){
            tag.putShort(name, (Short)value);
        }
        else if(value instanceof UUID){
            tag.putUUID(name, (UUID)value);
        }
        else if(value instanceof String){
            tag.putString(name, (String)value);
        }
    }
    public void readFromNBT(CompoundTag tag){
        if(value instanceof Integer){
            //Really odd way of making java not upset with me :/ Don't ask why.
            value = (T)(new SyncablePrimitive(tag.getInt(name),name).get());
        }
        else if(value instanceof Double){
            value = (T)(new SyncablePrimitive(tag.getDouble(name),name).get());
        }
        else if(value instanceof Boolean){
            value = (T)(new SyncablePrimitive(tag.getBoolean(name),name).get());
        }
        else if(value instanceof Float){
            value = (T)(new SyncablePrimitive(tag.getFloat(name),name).get());
        }
        else if(value instanceof Byte){
            value = (T)(new SyncablePrimitive(tag.getByte(name),name).get());
        }
        else if(value instanceof Long){
            value = (T)(new SyncablePrimitive(tag.getLong(name),name).get());
        }
        else if(value instanceof Short){
            value = (T)(new SyncablePrimitive(tag.getShort(name),name).get());
        }
        else if(value instanceof UUID){
            value = (T)(new SyncablePrimitive(tag.getUUID(name),name).get());
        }
        else if(value instanceof String){
            value = (T)(new SyncablePrimitive(tag.getString(name),name).get());
        }
        markDirty();
    }
}
