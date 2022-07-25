package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.DataEditor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;
import java.util.UUID;

/**
 * Not necessarily a primitive by java's standards but instead by MC's standards.
 */
public class SyncablePrimitive<T> extends SyncableValue<T> {
    private Capability<?> token = VCCapabilities.PRIMITIVE;
    public SyncablePrimitive(String KEY, T value){
        super(KEY, value);
    }
    public SyncablePrimitive(String KEY, T value, Capability<?> token){
        super(KEY, value);
        this.token=token;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataEditor destination){
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
    public void deserializeNBT(CompoundTag tag, DataEditor sender){
        if(value instanceof Integer){
            value = (T)(Integer)tag.getInt(KEY);
        }
        else if(value instanceof Double){
            value = (T)(Double)tag.getDouble(KEY);
        }
        else if(value instanceof Boolean){
            value = (T)(Boolean)tag.getBoolean(KEY);
        }
        else if(value instanceof Float){
            value = (T)(Float)tag.getFloat(KEY);
        }
        else if(value instanceof Byte){
            value = (T)(Byte)tag.getByte(KEY);
        }
        else if(value instanceof Long){
            value = (T)(Long)tag.getLong(KEY);
        }
        else if(value instanceof Short){
            value = (T)(Short)tag.getShort(KEY);
        }
        else if(value instanceof UUID){
            value = (T)tag.getUUID(KEY);
        }
        else if(value instanceof String){
            value = (T)tag.getString(KEY);
        }
        else if(value instanceof IntList){
            value = (T) new IntArrayList(tag.getIntArray(KEY));
        }
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(token);
    }
}
