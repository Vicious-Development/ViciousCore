package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IStringSerializable;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class SyncableHashMap<K extends IStringSerializable,V extends IVCNBTSerializable> extends SyncableValue<HashMap<K,V>> implements Map<K,V> {
    protected Supplier<K> keyConstructor;
    protected Supplier<V> valueConstructor;
    public SyncableHashMap(String key, Supplier<K> keyConstructor, Supplier<V> valueConstructor) {
        super(key, new HashMap<>());
        this.keyConstructor =keyConstructor;
        this.valueConstructor=valueConstructor;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag mapTag = new CompoundTag();
        value.forEach((k,v)->{
            CompoundTag value = new CompoundTag();
            v.serializeNBT(value,destination);
            mapTag.put(k.serializeString(),value);
        });
        tag.put(KEY,mapTag);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        CompoundTag mapTag = tag.getCompound(KEY);
        for (String key : mapTag.getAllKeys()) {
            K k = keyConstructor.get();
            V v = valueConstructor.get();
            k.deserializeString(key);
            v.deserializeNBT(mapTag.getCompound(key),sender);
            value.put(k,v);
        }
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
    public V get(Object key) {
        isDirty(true);
        return value.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        isDirty(true);
        return this.value.put(key,value);
    }

    @Override
    public V remove(Object key) {
        isDirty(true);
        return value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        value.putAll(m);
        isDirty(true);
    }

    @Override
    public void clear() {
        value.clear();
        isDirty(true);
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return value.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return value.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return value.entrySet();
    }
}
