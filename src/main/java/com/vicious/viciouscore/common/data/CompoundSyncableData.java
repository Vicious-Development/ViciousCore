package com.vicious.viciouscore.common.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class CompoundSyncableData implements ICapabilityProvider {
    protected LinkedHashMap<Capability<?>,SyncableData> tosync = new LinkedHashMap<>();
    public void forEachSyncable(Consumer<SyncableData> cons){
        for (Capability<?> capability : tosync.keySet()) {
            cons.accept(tosync.get(capability));
        }
    }
    public void updateClient(ServerPlayer player){
        forEachSyncable((s)->s.updateClient(player));
    }
    public void putIntoNBT(CompoundTag nbt){
        forEachSyncable((s)->putIntoNBT(nbt));
    }
    public void readFromNBT(CompoundTag nbt, DataEditor editor) {
        forEachSyncable((s)->s.readFromNBT(nbt,editor));
    }
    public <T extends SyncableData> T put(T data){
        tosync.put(data.getCapabilityToken(),data);
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        T sync = (T) tosync.get(cap);
        if(sync instanceof SidedSyncableData sided){
            if(sided.isAccessible(side)){
                return LazyOptional.of(()->sync);
            }
            return LazyOptional.empty();
        }
        else return LazyOptional.of(()->sync);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        T sync = (T)tosync.get(cap);
        if(sync == null) return LazyOptional.empty();
        return LazyOptional.of(()->sync);
    }
}
