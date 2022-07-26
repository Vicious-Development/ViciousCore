package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public class SyncableIVCNBT<T extends IVCNBTSerializable> extends SyncableValue<T>{
    public SyncableIVCNBT(String key, T defVal) {
        super(key, defVal);
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.IVCNBT);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        value.serializeNBT(inner,destination);
        tag.put(KEY,inner);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        CompoundTag inner = tag.getCompound(KEY);
        value.deserializeNBT(inner,sender);
    }
}
