package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import net.minecraft.nbt.CompoundTag;

public class SyncableEnum<T extends Enum<?>> extends SyncableValue<T>{
    private final T[] values;
    public SyncableEnum(String key, T defVal, T[] values) {
        super(key, defVal);
        this.values=values;
    }
    public SyncableEnum(String key, T[] values) {
        this(key, null,values);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        tag.putInt(KEY,value.ordinal());
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        value = values[tag.getInt(KEY)];
    }
}
