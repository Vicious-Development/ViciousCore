package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public abstract class SyncableValue<T> implements IVCCapabilityHandler, IVCNBTSerializable {
    /**
     * Only used by the server. Determines if the client will be synced.
     */
    protected boolean sendRemote = true;
    /**
     * Only used by the server. Determines if remote users can change server side data.
     */
    protected boolean readRemote = false;
    protected boolean isDirty = true;
    protected boolean shouldSave = true;
    protected boolean[] sideAccessibility;
    protected boolean valid;
    protected LazyOptional<SyncableValue<T>> lop;

    public final String KEY;

    public T value;

    public SyncableValue(String key, T defVal) {
        KEY = key;
        this.value=defVal;
        lop = LazyOptional.of(()->this);
    }

    public boolean canEdit(DataAccessor editor){
        return !(editor instanceof DataAccessor.Remote) || readRemote;
    }
    public <V extends SyncableValue<T>> V sendRemote(boolean sendRemote){
        this.sendRemote=sendRemote;
        return (V) this;
    }
    public <V extends SyncableValue<T>> V readRemote(boolean readRemote){
        this.readRemote=readRemote;
        return (V) this;
    }
    public <V extends SyncableValue<T>> V isDirty(boolean isDirty){
        this.isDirty=isDirty;
        return (V) this;
    }
    public <V extends SyncableValue<T>> V shouldSave(boolean shouldSave){
        this.shouldSave=shouldSave;
        return (V) this;
    }
    public <V extends SyncableValue<T>> V accessibleFromSides(Direction... dirs){
        if(this.sideAccessibility == null){
            sideAccessibility = new boolean[dirs.length];
        }
        for (Direction dir : dirs) {
            sideAccessibility[dir.ordinal()] = true;
        }
        return (V) this;
    }
    public <V extends SyncableValue<T>> V inaccessibleFromSides(Direction... dirs){
        if(this.sideAccessibility == null){
            sideAccessibility = new boolean[dirs.length];
        }
        for (Direction dir : dirs) {
            sideAccessibility[dir.ordinal()] = false;
        }
        return (V) this;
    }
    public <V extends SyncableValue<T>> V valid(boolean valid){
        this.valid=valid;
        if(!this.valid) lop.invalidate();
        return (V) this;
    }
    public boolean sideAccessible(Direction dir){
        if(sideAccessibility == null) return true;
        else{
            return sideAccessibility[dir.ordinal()];
        }
    }

    protected boolean changed() {
        return isDirty;
    }
    protected abstract List<Capability<?>> getCapabilityTokens();

    protected boolean shouldSend(DataAccessor destination) {
        return !(destination.isRemoteEditor()) || sendRemote;
    }


}
