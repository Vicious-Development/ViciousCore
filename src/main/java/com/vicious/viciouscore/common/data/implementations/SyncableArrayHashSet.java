package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouscore.common.util.ArrayHashSet;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Supplier;

public class SyncableArrayHashSet<T extends IVCNBTSerializable> extends SyncableValue<ArrayHashSet<T>> {
    private final Supplier<T> def;
    public SyncableArrayHashSet(String key, Supplier<T> def) {
        super(key, new ArrayHashSet<>());
        this.def = def;
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        tag = tag.getCompound(KEY);
        for (String key : tag.getAllKeys()) {
            T val = def.get();
            val.deserializeNBT(tag.getCompound(key),sender);
            value.add(val);
        }
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        for (int i = 0; i < value.size(); i++) {
            CompoundTag vtag = new CompoundTag();
            value.get(i).serializeNBT(vtag,destination);
            inner.put(""+i,vtag);
        }
    }}
