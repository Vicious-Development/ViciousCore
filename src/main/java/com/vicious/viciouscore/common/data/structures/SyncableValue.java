package com.vicious.viciouscore.common.data.structures;

import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.util.mixinsupport.InjectionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class SyncableValue<T> implements IVCCapabilityHandler, IVCNBTSerializable, InjectionInterface {
    private List<Runnable> onChange;
    public void listenChanged(Runnable run){
        if(onChange == null) onChange = new ArrayList<>();
        onChange.add(run);
    }
    public void stopListenChanged(Runnable run){
        if(onChange == null) onChange = new ArrayList<>();
        onChange.remove(run);
    }

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

    public final String KEY;

    public T value;
    protected SyncableValue<?> parent;

    public SyncableValue(String key, T defVal) {
        KEY = key;
        this.value=defVal;
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
        onParent((p)->p.isDirty(isDirty));
        if(isDirty) for (Runnable runnable : this.onChange) {
            runnable.run();
        }
        return (V) this;
    }
    public <V extends SyncableValue<T>> V shouldSave(boolean shouldSave){
        this.shouldSave=shouldSave;
        return (V) this;
    }

    protected boolean changed() {
        return isDirty;
    }

    protected boolean shouldSend(DataAccessor destination) {
        return !(destination.isRemoteEditor()) || sendRemote;
    }
    public T getValue(){
        return value;
    }
    public void setValue(T newVal){
        this.value=newVal;
        isDirty(true);
    }

    public boolean hasParent(){
        return parent != null;
    }
    public void onParent(Consumer<SyncableValue<?>> cons){
        if(hasParent()) cons.accept(parent);
    }



}
