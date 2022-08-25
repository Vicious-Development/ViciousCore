package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SyncableINBTCompound<T extends INBTSerializable<CompoundTag>> extends SyncableValue<T> {
    public SyncableINBTCompound(String key, T defVal) {
        super(key, defVal);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        if(value != null) {
            tag.put(KEY, value.serializeNBT());
        }
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        if(value != null) {
            value.deserializeNBT(tag.getCompound(KEY));
        }
    }
}
