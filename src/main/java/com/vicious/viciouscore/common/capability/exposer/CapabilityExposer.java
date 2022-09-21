package com.vicious.viciouscore.common.capability.exposer;

import com.vicious.viciouscore.common.capability.CapabilityHelper;
import com.vicious.viciouscore.common.capability.CapabilityMap;
import com.vicious.viciouscore.common.capability.combined.ICombinedCapabilityProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public class CapabilityExposer implements ICapabilityExposer {


    protected CapabilityMap<ICombinedCapabilityProvider<?>> map;
    public <T> void expose(T prov){
        Capability<T> token = (Capability<T>) CapabilityHelper.getTokenFor(prov);
        if(token != null){
            expose(token,prov);
        }
    }
    public <T> void expose(Capability<T> type, T prov){
        ICombinedCapabilityProvider<T> combined = (ICombinedCapabilityProvider<T>) map.get(type);
        if(combined == null){
            combined = CapabilityHelper.createCombinedProvider(type);
            map.put(type,combined);
        }
        combined.add(prov);
    }
    public <T> void conceal(T prov){
        Capability<T> token = (Capability<T>) CapabilityHelper.getTokenFor(prov);
        if(token != null){
            conceal(token,prov);
        }
    }
    public <T> void conceal(Capability<T> type, T prov){
        ICombinedCapabilityProvider<T> combined = (ICombinedCapabilityProvider<T>) map.get(type);
        if(combined != null) {
            combined.remove(prov);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return (LazyOptional<T>) map.get(cap).getLazyOptional();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return (LazyOptional<T>) map.get(cap).getLazyOptional();
    }
}
