package com.vicious.viciouscore.common.data.holder;

import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableValue;

public interface ISyncableCompoundHolder {
    SyncableCompound getData();
    default <T extends SyncableValue<?>> T sync(T sync){
        return (T) getData().add(sync);
    }
}
