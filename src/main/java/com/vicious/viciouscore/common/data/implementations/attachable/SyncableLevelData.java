package com.vicious.viciouscore.common.data.implementations.attachable;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SyncableLevelData extends SyncableAttachableCompound<Level> {
    public SyncableLevelData(Level l) {
        super("worlddata",l);
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == VCCapabilities.LEVELDATA) return (LazyOptional<T>) LazyOptional.of(()->this);
        return LazyOptional.empty();
    }

    public static void executeIfPresent(Object o, Consumer<SyncableLevelData> cons){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons);
        }
    }
    public static <V> void executeIfPresent(Object o, Consumer<V> cons, Class<V> as){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons,as);
        }
    }
    public static void executeIfPresent(ICapabilityProvider p, Consumer<SyncableLevelData> cons){
        LazyOptional<SyncableLevelData> lop = p.getCapability(VCCapabilities.LEVELDATA);
        SyncableLevelData data = FuckLazyOptionals.getOrNull(lop);
        if(data != null){
            cons.accept(data);
        }
    }
    public static <V> void executeIfPresent(ICapabilityProvider p, Consumer<V> cons, Class<V> as){
        LazyOptional<SyncableLevelData> lop = p.getCapability(VCCapabilities.LEVELDATA);
        SyncableLevelData data = FuckLazyOptionals.getOrNull(lop);
        if(as.isInstance(data)){
            cons.accept(as.cast(data));
        }
    }
}
