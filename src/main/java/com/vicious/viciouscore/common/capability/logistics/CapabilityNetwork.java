package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.implementations.SyncableArrayHashSet;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncablePrimitive;
import com.vicious.viciouscore.common.util.ArrayHashSet;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a network of capabilities of the same type.
 */
public class CapabilityNetwork<T> implements ISyncableCompoundHolder{
    private final SyncableCompound data = new SyncableCompound("capnet").sendRemote(false);
    private final SyncablePrimitive<UUID> uuid;
    private final SyncableArrayHashSet<CapabilityConnection<T>> hashSet;
    public CapabilityNetwork(Function<Tag, CapabilityConnection<T>> deserializer){
        this.uuid = sync(new SyncablePrimitive<>("id",UUID.randomUUID()));
        this.hashSet = sync(new SyncableArrayHashSet<>("cset",deserializer));
    }

    public void addConnection(LazyOptional<T> capability, SidedWorldPos pos){
         CapabilityConnection<T> cc = new CapabilityConnection<>(capability,pos);
         if(cc.valid()) getConnections().add(cc);
    }
    public void forEachConnection(Consumer<T> cons){
        List<CapabilityConnection<T>> toRemove = new ArrayList<>();
        for (int i = 0; i < getConnections().size(); i++) {
            CapabilityConnection<T> cc = getConnections().get(i);
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
            CapabilityConnection<T> cc = getConnections().get(i);
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

    public void removeConnection(LazyOptional<T> capability, SidedWorldPos pos) {
        CapabilityConnection<T> cc = new CapabilityConnection<>(capability,pos);
        if(cc.valid()) getConnections().remove(cc);
    }

    @Override
    public SyncableCompound getData() {
        return data;
    }
    public ArrayHashSet<CapabilityConnection<T>> getConnections(){
        return hashSet.value;
    }
    public UUID getUUID(){
        return uuid.value;
    }
}
