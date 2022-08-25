package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class SyncableINBT<T extends INBTSerializable<Tag>> extends SyncableValue<T> {
    public SyncableINBT(String key, T defVal) {
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
            value.deserializeNBT(tag.get(KEY));
        }
    }
}
