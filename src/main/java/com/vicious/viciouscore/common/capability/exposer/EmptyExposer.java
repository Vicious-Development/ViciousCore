package com.vicious.viciouscore.common.capability.exposer;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyExposer implements ICapabilityExposer{
    public <T> void expose(T prov){
    }
    public  <T> void expose(Capability<T> type, T prov){
    }
    public  <T> void conceal(T prov){
    }
    public <T> void conceal(Capability<T> type, T prov){
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return LazyOptional.empty();
    }
}
