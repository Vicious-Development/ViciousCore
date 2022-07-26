package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.DataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class SyncableINBTCompound<T extends INBTSerializable<CompoundTag>> extends SyncableValue<T> {
    public SyncableINBTCompound(String key, T defVal) {
        super(key, defVal);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        tag.put(KEY,value.serializeNBT());
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        value.deserializeNBT(tag.getCompound(KEY));
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.INBT);
    }

}
