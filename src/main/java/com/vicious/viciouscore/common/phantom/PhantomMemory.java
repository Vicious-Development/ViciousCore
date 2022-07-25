package com.vicious.viciouscore.common.phantom;

import com.vicious.viciouscore.common.data.DataEditor;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PhantomMemory implements ISyncableCompoundHolder {
    protected final SyncableCompound data = new SyncableCompound("phdat").readRemote(true);
    public Set<Object> keySet = new HashSet<>();
    protected int ID;


    public PhantomMemory(Object... associations){
        this.keySet.addAll(Arrays.asList(associations));
    }

    public void associate(WorldPosition position){
        keySet.add(position);
    }
    public void disassociate(WorldPosition position){
        keySet.remove(position);
    }

    @Override
    public SyncableCompound getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhantomMemory that = (PhantomMemory) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public void serializeNBT(CompoundTag tag, DataEditor editor) {
        data.serializeNBT(tag,editor);
    }

    public void deserializeNBT(CompoundTag nbt, DataEditor editor) {
        data.deserializeNBT(nbt,editor);
    }
}
