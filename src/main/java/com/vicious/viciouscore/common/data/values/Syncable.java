package com.vicious.viciouscore.common.data.values;


import net.minecraft.nbt.CompoundTag;


/**
 * A packet writable value that can be read by both client and server and sometimes edited by either side.
 */
public abstract class Syncable<T> {
    protected boolean dirty;
    public boolean doSave = true;
    protected boolean alwaysDirty = false;
    protected boolean clientEditable = false;
    protected boolean clientReadable = true;

    protected void markDirty() {
        dirty = true;
    }
    public abstract void readFromNBT(CompoundTag nbtTagCompound);
    public abstract void putIntoNBT(CompoundTag tag);

    public <V extends Syncable<T>> V setClientEditable(boolean value){
        clientEditable=value;
        return (V) this;
    }
    public <V extends Syncable<T>> V setClientReadable(boolean value){
        clientReadable=value;
        return (V) this;
    }


    /**
     * Prevents the syncable from being saved to chunk NBT. Useful for if the data has a default setting and/or a setting determined by another piece of data.
     * This still allows the client to receive the Syncable information it needs to remain synced.
     * @param s
     * @param <S>
     * @return
     */
    public static <S extends Syncable> S doNotSave(S s) {
        s.doSave = false;
        return s;
    }
    public static <S extends Syncable> S alwaysDirty(S s){
        s.alwaysDirty = true;
        return s;
    }

    /**
     * Determines if the data will be sent to the client. Do note that even if a value is readable, it may not be editable and vice versa.
     */
    public boolean clientReadable() {
        return clientReadable;
    }
    /**
     * Whether the value can be modified by the client.
     */
    public boolean clientEditable(){
        return clientEditable;
    }

}
