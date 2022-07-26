package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.logistics.SidedWorldPos;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;

public class SyncableSidedWorldPos extends SyncableINBTCompound<SidedWorldPos> {
    public SyncableSidedWorldPos(String key, SidedWorldPos defVal) {
        super(key, defVal);
    }
}
