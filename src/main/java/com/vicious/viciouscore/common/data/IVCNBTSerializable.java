package com.vicious.viciouscore.common.data;

import net.minecraft.nbt.CompoundTag;

public interface IVCNBTSerializable {
    void serializeNBT(CompoundTag tag, DataAccessor destination);
    void deserializeNBT(CompoundTag tag, DataAccessor sender);
}
