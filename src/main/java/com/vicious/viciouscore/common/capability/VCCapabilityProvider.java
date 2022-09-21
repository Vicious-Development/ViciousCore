package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.exposer.CapabilityExposer;
import com.vicious.viciouscore.common.capability.exposer.ICapabilityExposer;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class VCCapabilityProvider implements ICapabilityProvider {
    public static final AllExposers ALL_EXPOSERS_KEY = new AllExposers();
    private final Map<Object,ICapabilityExposer> exposers = new HashMap<>();

    public VCCapabilityProvider(){
        exposers.put(ALL_EXPOSERS_KEY,new CapabilityExposer());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getExposer(side).getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return getExposer().getCapability(cap);
    }
    /**
     * Returns the exposer mapped to ALL_EXPOSERS_KEY.
     */
    public ICapabilityExposer getExposer(){
        return exposers.get(ALL_EXPOSERS_KEY);
    }

    /**
     * If a null key is provided, returns the universal exposer.
     * Else returns the exposer mapped to the key if there is one.
     */
    public ICapabilityExposer getExposer(Object key){
        if(key == null) return getExposer();
        else return exposers.getOrDefault(key, ICapabilityExposer.EMPTY);
    }

    private static class AllExposers{
        @Override
        public boolean equals(Object obj) {
            return obj==this;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
