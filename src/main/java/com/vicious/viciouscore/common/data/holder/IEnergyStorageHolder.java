package com.vicious.viciouscore.common.data.holder;

import com.vicious.viciouscore.common.data.implementations.SyncableEnergyStorage;

public interface IEnergyStorageHolder extends ISyncableCompoundHolder{
    SyncableEnergyStorage getEnergyStorage();
}
