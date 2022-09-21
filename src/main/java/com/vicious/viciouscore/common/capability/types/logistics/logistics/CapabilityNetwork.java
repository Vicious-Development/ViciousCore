package com.vicious.viciouscore.common.capability.types.logistics.logistics;

import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.implementations.SyncableArrayHashSet;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncablePrimitive;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouscore.common.util.ArrayHashSet;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a network of capabilities of the same type.
 */
public class CapabilityNetwork<T> implements ISyncableCompoundHolder{
    private final SyncableCompound data = new SyncableCompound("capnet").sendRemote(false);
    private final SyncablePrimitive<UUID> uuid;
    private final SyncableArrayHashSet<CapabilityConnection<? extends SyncableValue<?>,T>> hashSet;
    public CapabilityNetwork(Supplier<CapabilityConnection<? extends SyncableValue<?>,T>> constructor){
        this.uuid = sync(new SyncablePrimitive<>("id",UUID.randomUUID()));
        this.hashSet = sync(new SyncableArrayHashSet<>("cset",constructor));
    }

    public void addConnection(CapabilityConnection cc){
         if(cc.valid()) getConnections().add(cc);
    }
    public void forEachConnection(Consumer<T> cons){
        for (int i = 0; i < getConnections().size(); i++) {
            CapabilityConnection<? extends SyncableValue<?>,T> cc = getConnections().get(i);
            if(cc.valid()){
                T t = cc.get();
                if(t != null) cons.accept(t);
            }
            else{
                getConnections().remove(i);
                i--;
            }
        }
    }
    public void forEachConnection(Consumer<T> cons, Predicate<T> escape){
        for (int i = 0; i < getConnections().size(); i++) {
            CapabilityConnection<? extends SyncableValue<?>,T> cc = getConnections().get(i);
            if(cc.valid()){
                T t = cc.get();
                if(t != null){
                    if(escape.test(t)) return;
                    cons.accept(t);
                }
            }
            else{
                getConnections().remove(i);
                i--;
            }
        }
    }
    public CapabilityNetwork<T> concat(CapabilityNetwork<T> network){
        if(network == null) return this;
        this.getConnections().addAll(network.getConnections());
        return this;
    }

    public void removeConnection(CapabilityConnection cc) {
        getConnections().remove(cc);
    }

    @Override
    public SyncableCompound getData() {
        return data;
    }
    public ArrayHashSet<CapabilityConnection<? extends SyncableValue<?>,T>> getConnections(){
        return hashSet.value;
    }
    public UUID getUUID(){
        return uuid.value;
    }
}
