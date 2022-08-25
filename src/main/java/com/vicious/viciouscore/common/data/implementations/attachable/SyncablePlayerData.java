package com.vicious.viciouscore.common.data.implementations.attachable;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.capability.interfaces.ICapabilityDeathPersistant;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SyncablePlayerData extends SyncableAttachableCompound<Player> implements ICapabilityDeathPersistant {
    public SyncablePlayerData(Player p) {
        super("playerdata",p);
    }

    @Override
    public void copyTo(ICapabilityDeathPersistant copy) {
        if(copy instanceof SyncableCompound comp){
            CompoundTag tag = new CompoundTag();
            comp.serializeNBT(tag,DataAccessor.WORLD);
            deserializeNBT(tag,DataAccessor.WORLD);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == VCCapabilities.PLAYERDATA) return (LazyOptional<T>) LazyOptional.of(()->this);
        return LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        deserializeNBT(nbt,DataAccessor.WORLD);
        MinecraftForge.EVENT_BUS.post(new Load(this));
    }

    public static class Load extends Event{
        public final SyncablePlayerData data;
        public Load(SyncablePlayerData data){
            this.data=data;
        }
    }

    public static void executeIfPresent(Object o, Consumer<SyncablePlayerData> cons){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons);
        }
    }
    public static <V> void executeIfPresent(Object o, Consumer<V> cons, Class<V> as){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons,as);
        }
    }
    public static void executeIfPresent(ICapabilityProvider p, Consumer<SyncablePlayerData> cons){
        LazyOptional<SyncablePlayerData> lop = p.getCapability(VCCapabilities.PLAYERDATA);
        SyncablePlayerData data = FuckLazyOptionals.getOrNull(lop);
        if(data != null){
            cons.accept(data);
        }
    }
    public static <V> void executeIfPresent(ICapabilityProvider p, Consumer<V> cons, Class<V> as){
        LazyOptional<SyncablePlayerData> lop = p.getCapability(VCCapabilities.PLAYERDATA);
        SyncablePlayerData data = FuckLazyOptionals.getOrNull(lop);
        if(as.isInstance(data)){
            cons.accept(as.cast(data));
        }
    }
}
