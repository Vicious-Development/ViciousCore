package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.state.TickState;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public class SyncableTickState extends SyncableINBTCompound<TickState> {
    public SyncableTickState(String key) {
        super(key, new TickState(0));
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.TICKABLE);
    }
}
