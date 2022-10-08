package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.network.packets.datasync.CPacketSyncData;
import com.vicious.viciouscore.common.network.packets.datasync.SPacketSyncData;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SyncableCompound extends SyncableValue<LinkedHashMap<String, SyncableValue<?>>> implements Map<String, SyncableValue<?>>, ISyncableCompoundHolder {
    public SyncableCompound(String key) {
        super(key, new LinkedHashMap<>());
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        for (SyncableValue<?> syncableValue : values()) {
            if(destination == DataAccessor.WORLD){
                syncableValue.serializeNBT(inner,destination);
            }
            else if(syncableValue.changed() && syncableValue.shouldSend(destination)){
                syncableValue.isDirty(false);
                syncableValue.serializeNBT(inner,destination);
            }
        }
        tag.put(KEY,inner);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        tag = tag.getCompound(KEY);
        for (SyncableValue<?> syncableValue : values()) {
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


    public void forEachSyncable(Consumer<SyncableValue<?>> cons) {
        for (SyncableValue<?> val : values()) {
            cons.accept(val);

        }
    }

    public <V extends SyncableValue<?>> V add(V val){
        put(val.KEY,val);
        val.parent=this;
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
    public SyncableValue<?> get(Object key) {
        return value.get(key);
    }

    @Nullable
    @Override
    public SyncableValue<?> put(String key, SyncableValue<?> value) {
        return this.value.put(key,value);
    }

    @Override
    public SyncableValue<?> remove(Object key) {
        return value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends SyncableValue<?>> m) {
        value.putAll(m);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    @NotNull
    @Override
    public Collection<SyncableValue<?>> values() {
        return value.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, SyncableValue<?>>> entrySet() {
        return value.entrySet();
    }

    public void syncRemote(SyncTarget target){
        CompoundTag write = new CompoundTag();
        serializeNBT(write,target.editor);
        if(target.editor.isRemoteEditor()){
            if(target instanceof SyncTarget.Window twindow) {
                target.editor.sendPacket(new CPacketSyncData.Window(twindow,write));
            }
        }
        else{
            if(target instanceof SyncTarget.Window twindow) {
                target.editor.sendPacket(new SPacketSyncData.Window(twindow,write));
            }
        }
    }

    @Override
    public SyncableCompound getData() {
        return this;
    }
}
