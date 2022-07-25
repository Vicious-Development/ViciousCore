package com.vicious.viciouscore.common.data.holder;

import com.vicious.viciouscore.common.data.implementations.SyncableInventory;

public interface IItemIODataHolder extends ISyncableCompoundHolder{
    SyncableInventory getItemOutput();
    SyncableInventory getItemInput();
}
