package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.common.data.storage.Syncable;
import com.vicious.viciouscore.common.data.storage.SyncablePrimitive;

public class SyncableTickableData extends SyncableData {
    public SyncablePrimitive<Integer> tickTimeElapsed = Syncable.alwaysDirty(new SyncablePrimitive<>(0,"tickTimeElasped"));
    public SyncablePrimitive<Integer> tickTimeForCompletion = Syncable.alwaysDirty(new SyncablePrimitive<>(0,"tickTimeForCompletion"));
    public SyncableTickableData(){
        super();
        track(tickTimeElapsed);
        track(tickTimeForCompletion);
    }
}
