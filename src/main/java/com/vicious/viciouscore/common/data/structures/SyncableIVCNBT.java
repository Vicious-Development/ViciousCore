package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class SyncableIVCNBT<T extends IVCNBTSerializable> extends SyncableValue<T>{
    public SyncableIVCNBT(String key, T defVal) {
        super(key, defVal);
    }


    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        if(value != null) {
            CompoundTag inner = new CompoundTag();
            value.serializeNBT(inner, destination);
            tag.put(KEY, inner);
        }
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        if(value != null) {
            CompoundTag inner = tag.getCompound(KEY);
            value.deserializeNBT(inner, sender);
        }
    }
}
