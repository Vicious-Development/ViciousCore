package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class CapabilityConnection<T> implements ISyncableCompoundHolder {
    private final SyncableCompound data = new SyncableCompound("capcon");
    private final SyncableINBTCompound<SidedWorldPos> pos;

    private final LazyOptional<T> optional;
    private boolean valid;


    public CapabilityConnection(LazyOptional<T> optional, SidedWorldPos pos){
        this.optional=optional;
        this.pos = sync(new SyncableINBTCompound<>("pos",pos));
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
        CapabilityConnection<?> that = (CapabilityConnection<?>) o;
        return Objects.equals(pos.value, that.pos.value) && Objects.equals(optional, that.optional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos.value, optional);
    }

    @Override
    public SyncableCompound getData() {
        return data;
    }
}
