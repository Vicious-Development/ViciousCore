package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.state.TickState;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;

public class SyncableTickState extends SyncableINBTCompound<TickState> {
    public SyncableTickState(String key) {
        super(key, new TickState(0));
    }

}
