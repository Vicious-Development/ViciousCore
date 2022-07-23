package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.values.Syncable;
import com.vicious.viciouscore.common.data.values.SyncablePrimitive;
import net.minecraftforge.common.capabilities.Capability;

public class SyncableTickableData extends SyncableData {
    public SyncablePrimitive<Integer> tickTimeElapsed = Syncable.alwaysDirty(new SyncablePrimitive<>(0,"ticks"));
    public SyncablePrimitive<Integer> tickTimeForCompletion = Syncable.alwaysDirty(new SyncablePrimitive<>(0,"requiredticks"));
    public SyncableTickableData(){
        super();
        track(tickTimeElapsed);
        track(tickTimeForCompletion);
    }

    @Override
    public Capability<?> getCapabilityToken() {
        return VCCapabilities.TICKABLE;
    }

}
