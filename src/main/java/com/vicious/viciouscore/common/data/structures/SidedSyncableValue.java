package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.data.DataAccessor;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class SidedSyncableValue<T extends SyncableValue<?>> extends SyncableValue<T> {
    private final boolean[] accessibilities = new boolean[Direction.values().length];
    public void setAccessible(Direction side){
        accessibilities[side.ordinal()] = true;
        isDirty(true);
    }
    public void setInaccessible(Direction side){
        accessibilities[side.ordinal()] = false;
        isDirty(true);
    }
    public boolean accessible(Direction side){
        return accessibilities[side.ordinal()];
    }

    public SidedSyncableValue(String key, T val) {
        super(key,val);
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        IntList out = new IntArrayList();
        for (int i = 0; i < accessibilities.length; i++) {
            if(accessibilities[i]) out.add(i);
        }
        inner.putIntArray("s",out);
        value.serializeNBT(inner,destination);
        tag.put(KEY,inner);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        CompoundTag inner = tag.getCompound(KEY);
        int[] in = inner.getIntArray("s");
        for (int i : in) {
            accessibilities[i]=true;
        }
        value.deserializeNBT(inner,sender);
    }

}
