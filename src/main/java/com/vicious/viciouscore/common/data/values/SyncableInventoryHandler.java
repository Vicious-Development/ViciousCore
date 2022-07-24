package com.vicious.viciouscore.common.data.values;

import com.vicious.viciouscore.common.inventory.FastItemStackHandler;

public class SyncableInventoryHandler extends SyncableObject<FastItemStackHandler>{
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
}
