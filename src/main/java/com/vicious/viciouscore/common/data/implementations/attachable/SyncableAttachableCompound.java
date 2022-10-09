package com.vicious.viciouscore.common.data.implementations.attachable;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SyncableAttachableCompound<T extends ICapabilityProvider> extends SyncableCompound implements ICapabilitySerializable<CompoundTag> {
    protected final T attached;
    public SyncableAttachableCompound(String key, T attached) {
        super(key);
        this.attached=attached;
    }
    public T getHolder(){
        return attached;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag out = new CompoundTag();
        serializeNBT(out, DataAccessor.WORLD);
        return out;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        deserializeNBT(nbt, DataAccessor.WORLD);
    }


}
