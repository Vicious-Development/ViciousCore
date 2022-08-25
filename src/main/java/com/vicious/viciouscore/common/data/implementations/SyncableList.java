package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncableList<T extends IVCNBTSerializable> extends SyncableValue<List<T>> {
    private final Supplier<T> constructor;
    public SyncableList(String key, Supplier<T> constructor) {
        super(key, new ArrayList<>());
        this.constructor=constructor;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        for (int i = 0; i < value.size(); i++) {
            CompoundTag elementTag = new CompoundTag();
            value.get(i).serializeNBT(elementTag,destination);
            inner.put("e"+i,elementTag);
        }
        tag.put(KEY,inner);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        CompoundTag inner = tag.getCompound(KEY);
        for (String key : inner.getAllKeys()) {
            T t = constructor.get();
            t.deserializeNBT(inner.getCompound(key),sender);
            value.add(t);
        }
    }
}
