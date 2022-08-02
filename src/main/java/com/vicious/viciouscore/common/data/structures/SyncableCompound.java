package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.network.packets.datasync.CPacketSyncData;
import com.vicious.viciouscore.common.network.packets.datasync.SPacketSyncData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SyncableCompound extends SyncableValue<LinkedHashMap<Capability<?>, List<? extends SyncableValue<?>>>> implements Map<Capability<?>, List<? extends SyncableValue<?>>>, ICapabilityProvider {
    public SyncableCompound(String key) {
        super(key, new LinkedHashMap<>());
    }
    private List<Runnable> onChange = new ArrayList<>();

    @Override
    public <V extends SyncableValue<LinkedHashMap<Capability<?>, List<? extends SyncableValue<?>>>>> V isDirty(boolean isDirty) {
        V ret = super.isDirty(isDirty);
        if(isDirty) for (Runnable runnable : this.onChange) {
            runnable.run();
        }
        return ret;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        for (Capability<?> capability : keySet()) {
            for (SyncableValue<?> syncableValue : get(capability)) {
                if(syncableValue.changed() && syncableValue.shouldSend(destination)){
                    syncableValue.isDirty(false);
                    syncableValue.serializeNBT(inner,destination);
                }
            }
        }
        tag.put(KEY,inner);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        tag = tag.getCompound(KEY);
        for (Capability<?> capability : keySet()) {
            for (SyncableValue<?> syncableValue : get(capability)) {
                if(tag.contains(syncableValue.KEY)){
                    if(syncableValue.canEdit(sender)) {
                        syncableValue.deserializeNBT(tag,sender);
                    }
                    else {
                        sender.securityViolated("Reason: Attempted to modify a value they don't have authorization to edit. This is a sign of attempting to hack!");
                    }
                }
            }
        }
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.COMPOUND);
    }

    public void forEachSyncable(Consumer<SyncableValue<?>> cons) {
        for (Capability<?> capability : keySet()) {
            for (SyncableValue<?> val : get(capability)) {
                cons.accept(val);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        for (SyncableValue<?> val : get(cap)) {
            if(val.sideAccessible(side)){
                return (LazyOptional<T>) val.lop;
            }
        }
        return LazyOptional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        for (SyncableValue<?> val : get(cap)) {
            return (LazyOptional<T>) val.lop;
        }
        return LazyOptional.empty();
    }

    @Override
    public <V extends SyncableValue<LinkedHashMap<Capability<?>, List<? extends SyncableValue<?>>>>> V valid(boolean valid) {
        forEachSyncable((s)->s.valid(valid));
        return super.valid(valid);
    }

    public <V extends SyncableValue<?>> V add(V val){
        for (Capability<?> token : val.getCapabilityTokens()) {
            List vals = get(token);
            if(vals == null){
                vals = new ArrayList();
                put(token,vals);
            }
            val.parent = this;
            vals.add(val);
        }
        return val;
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return value.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public List<? extends SyncableValue<?>> get(Object key) {
        return value.get(key);
    }

    @Nullable
    @Override
    public List<? extends SyncableValue<?>> put(Capability<?> key, List<? extends SyncableValue<?>> value) {
        return this.value.put(key,value);
    }

    @Override
    public List<? extends SyncableValue<?>> remove(Object key) {
        return value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends Capability<?>, ? extends List<? extends SyncableValue<?>>> m) {
        value.putAll(m);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @NotNull
    @Override
    public Set<Capability<?>> keySet() {
        return value.keySet();
    }

    @NotNull
    @Override
    public Collection<List<? extends SyncableValue<?>>> values() {
        return value.values();
    }

    @NotNull
    @Override
    public Set<Entry<Capability<?>, List<? extends SyncableValue<?>>>> entrySet() {
        return value.entrySet();
    }

    public void listenChanged(Runnable run){
        onChange.add(run);
    }
    public void stopListenChanged(Runnable run){
        onChange.add(run);
    }

    public void syncRemote(SyncTarget target){
        CompoundTag write = new CompoundTag();
        serializeNBT(write,target.editor);
        if(target.editor.isRemoteEditor()){
            if(target instanceof SyncTarget.Window twindow) {
                target.editor.sendPacket(new CPacketSyncData.Window(twindow,write));
            }
            if(target instanceof SyncTarget.Tile ttile) {
                target.editor.sendPacket(new CPacketSyncData.Tile(ttile, write));
            }
        }
        else{
            if(target instanceof SyncTarget.Window twindow) {
                target.editor.sendPacket(new SPacketSyncData.Window(twindow,write));
            }
            if(target instanceof SyncTarget.Tile ttile){
                target.editor.sendPacket(new SPacketSyncData.Tile(ttile,write));
            }
        }
    }

}
