package com.vicious.viciouscore.common.data.values;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SyncableObject<T extends INBTSerializable<CompoundTag>> extends SyncableValue<T>{
    public SyncableObject(T value, String name) {
        super(value, name);
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        value.deserializeNBT(nbtTagCompound);
    }

    @Override
    public void putIntoNBT(CompoundTag tag) {
        tag.put(name,value.serializeNBT());
    }
}
