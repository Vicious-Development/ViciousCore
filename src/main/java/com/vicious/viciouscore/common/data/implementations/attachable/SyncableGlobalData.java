package com.vicious.viciouscore.common.data.implementations.attachable;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import com.vicious.viciouscore.common.util.server.ServerHelper;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SyncableGlobalData extends SyncableAttachableCompound<Level> {
    private static SyncableGlobalData instance;

    public static @NotNull SyncableGlobalData getInstance(){
        if(instance == null) instance = Aunotamation.processObject(new SyncableGlobalData(ServerHelper.getMainLevel()));
        return instance;
    }
    public SyncableGlobalData(Level holder) {
        super("globaldata",holder);
    }

    //Only load this data once.
    private boolean load = true;

    public static void purgeInstance() {
        instance = null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(load) {
            load = false;
            super.deserializeNBT(nbt);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == VCCapabilities.GLOBALDATA) return (LazyOptional<T>) LazyOptional.of(()->this);
        return LazyOptional.empty();
    }

    public static void executeIfPresent(Object o, Consumer<SyncableGlobalData> cons){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons);
        }
    }
    public static <V> void executeIfPresent(Object o, Consumer<V> cons, Class<V> as){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons,as);
        }
    }
    public static void executeIfPresent(ICapabilityProvider p, Consumer<SyncableGlobalData> cons){
        LazyOptional<SyncableGlobalData> lop = p.getCapability(VCCapabilities.GLOBALDATA);
        SyncableGlobalData data = FuckLazyOptionals.getOrNull(lop);
        if(data != null){
            cons.accept(data);
        }
    }
    public static <V> void executeIfPresent(ICapabilityProvider p, Consumer<V> cons, Class<V> as){
        LazyOptional<SyncableGlobalData> lop = p.getCapability(VCCapabilities.GLOBALDATA);
        SyncableGlobalData data = FuckLazyOptionals.getOrNull(lop);
        if(as.isInstance(data)){
            cons.accept(as.cast(data));
        }
    }
}
