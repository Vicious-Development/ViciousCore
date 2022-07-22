package com.vicious.viciouscore.common.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttachableCapabilityProvider<CAP,TARGET> implements ICapabilityProvider {
    private final LazyOptional<CAP> provider;
    private final Capability<CAP> token;
    public final TARGET target;
    public AttachableCapabilityProvider(LazyOptional<CAP> provider, Capability<CAP> token, TARGET target){
        this.provider=provider;
        this.target=target;
        this.token=token;
    }
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(token)) return (LazyOptional<T>) provider;
        return LazyOptional.empty();
    }
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap.equals(token)) return (LazyOptional<T>) provider;
        return LazyOptional.empty();
    }

}
