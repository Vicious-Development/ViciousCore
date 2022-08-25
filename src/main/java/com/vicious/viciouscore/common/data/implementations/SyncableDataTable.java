package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.state.DataTable;
import com.vicious.viciouscore.common.data.state.IDataTable;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import net.minecraft.nbt.CompoundTag;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncableDataTable<T extends IVCNBTSerializable> extends SyncableValue<DataTable<T>> implements IDataTable<T> {
    private Supplier<T> constructor;
    public SyncableDataTable(String key, Supplier<T> constructor) {
        super(key, new DataTable<>());
        this.constructor = constructor;
    }

    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag inner = new CompoundTag();
        AtomicInteger i = new AtomicInteger(0);
        forEach((data)->{
            CompoundTag elementTag = new CompoundTag();
            data.serializeNBT(elementTag, destination);
            inner.put("e" + i.get(), elementTag);
            i.getAndAdd(1);
        });
        tag.put(this.KEY, inner);
    }

    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        CompoundTag inner = tag.getCompound(this.KEY);
        for (String key : inner.getAllKeys()) {
            T t = this.constructor.get();
            t.deserializeNBT(inner.getCompound(key),sender);
            add(t);
        }
    }

    @Override
    public SyncableDataTable<T> supports(Function<T, Object> keyfunc, Class<?> keyClass) {
        value.supports(keyfunc,keyClass);
        return this;
    }

    @Override
    public void add(T t) {
        value.add(t);
        isDirty(true);
    }

    @Override
    public void remove(T t) {
        value.remove(t);
        isDirty(true);
    }

    @Override
    public boolean containsValue(T t) {
        return value.containsValue(t);
    }

    @Override
    public boolean containsKey(Object o) {
        return value.containsKey(o);
    }

    @Override
    public T get(Object o) {
        return value.get(o);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        value.forEach(consumer);
    }

    @Override
    public int size() {
        return value.size();
    }
}
