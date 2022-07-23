package com.vicious.viciouscore.common.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record AttachableCapabilityProvider<CAP, TARGET>(
        LazyOptional<CAP> provider,
        Capability<CAP> token,
        TARGET target)
implements ICapabilityProvider {

    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(token)) return (LazyOptional<T>) provider;
        return LazyOptional.empty();
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap.equals(token)) return (LazyOptional<T>) provider;
        return LazyOptional.empty();
    }

}
