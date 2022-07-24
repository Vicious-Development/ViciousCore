package com.vicious.viciouscore.common.data.values;

import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import net.minecraft.nbt.CompoundTag;

public class SyncableInventoryHandler extends SyncableValue<FastItemStackHandler>{
    public SyncableInventoryHandler(int size, String name) {
        super(new FastItemStackHandler(size), name);
    }

    /**
     * Making sure that no one ever modifies this value and makes packet exploits possible
     */
    @Override
    public boolean clientEditable() {
        return false;
    }

    @Override
    public void readFromNBT(CompoundTag nbtTagCompound) {
        value.deserializeNBT(nbtTagCompound);
    }

    @Override
    public void putIntoNBT(CompoundTag tag) {
        tag.put(name,value.serializeNBT());
    }

}
