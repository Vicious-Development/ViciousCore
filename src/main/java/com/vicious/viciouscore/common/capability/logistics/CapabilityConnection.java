package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class CapabilityConnection<KEYTYPE extends SyncableValue<?>,T> implements ISyncableCompoundHolder, IVCNBTSerializable {
    protected final SyncableCompound data = new SyncableCompound("capcon");
    protected KEYTYPE key;

    protected LazyOptional<T> optional;
    protected boolean valid;

    public CapabilityConnection(KEYTYPE key){
        this.key=sync(key);
    }


    public CapabilityConnection(LazyOptional<T> optional, KEYTYPE key){
        this.optional=optional;
        this.key = sync(key);
        valid=optional.isPresent();
        optional.addListener(this::onInvalidate);
    }
    public void onInvalidate(LazyOptional<T> opt){
        valid=false;
    }
    public boolean valid(){
        return valid;
    }
    public T get(){
        return FuckLazyOptionals.getOrNull(optional);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapabilityConnection<?,?> that = (CapabilityConnection<?,?>) o;
        return Objects.equals(key.value, that.key.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key.value);
    }

    @Override
    public SyncableCompound getData() {
        return data;
    }
    public KEYTYPE getKey(){
        return key;
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        data.serializeNBT(tag,destination);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        data.deserializeNBT(tag,sender);
    }
}
