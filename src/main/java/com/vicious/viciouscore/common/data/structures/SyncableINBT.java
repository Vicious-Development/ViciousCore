package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.DataEditor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class SyncableINBT<T extends INBTSerializable<Tag>> extends SyncableValue<T> {
    public SyncableINBT(String key, T defVal) {
        super(key, defVal);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataEditor destination) {
        tag.put(KEY,value.serializeNBT());
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataEditor sender) {
        value.deserializeNBT(tag.get(KEY));
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.INBT);
    }
}
