package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * Not necessarily a primitive by java's standards but instead by MC's standards.
 */
public class SyncablePrimitive<T> extends SyncableValue<T> {
    private final Class<?> type;
    public SyncablePrimitive(String KEY, @NonNull T value){
        super(KEY, value);
        type=value.getClass();
    }
    public SyncablePrimitive(String KEY, @Nullable T value, Class<T> cls){
        super(KEY, value);
        type=cls;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination){
        if(value instanceof Integer v){
            tag.putInt(KEY, v);
        }
        else if(value instanceof Double v){
            tag.putDouble(KEY, v);
        }
        else if(value instanceof Boolean v){
            tag.putBoolean(KEY, v);
        }
        else if(value instanceof Float v){
            tag.putFloat(KEY, v);
        }
        else if(value instanceof Byte v){
            tag.putByte(KEY, v);
        }
        else if(value instanceof Long v){
            tag.putLong(KEY, v);
        }
        else if(value instanceof Short v){
            tag.putShort(KEY, v);
        }
        else if(value instanceof UUID v){
            tag.putUUID(KEY, v);
        }
        else if(value instanceof String v){
            tag.putString(KEY, v);
        }
        else if(value instanceof IntList v){
            tag.putIntArray(KEY,v.toArray(new int[0]));
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender){
        if(type == Integer.class){
            value = (T)(Integer)tag.getInt(KEY);
        }
        else if(type == Double.class){
            value = (T)(Double)tag.getDouble(KEY);
        }
        else if(type == Boolean.class){
            value = (T)(Boolean)tag.getBoolean(KEY);
        }
        else if(type == Float.class){
            value = (T)(Float)tag.getFloat(KEY);
        }
        else if(type == Byte.class){
            value = (T)(Byte)tag.getByte(KEY);
        }
        else if(type == Long.class){
            value = (T)(Long)tag.getLong(KEY);
        }
        else if(type == Short.class){
            value = (T)(Short)tag.getShort(KEY);
        }
        else if(type == UUID.class){
            value = (T)tag.getUUID(KEY);
        }
        else if(type == String.class){
            value = (T)tag.getString(KEY);
        }
        else if(type == IntList.class){
            value = (T) new IntArrayList(tag.getIntArray(KEY));
        }
    }
}
