package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.logistics.SidedWorldPos;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.phantom.WorldPos;

public class SyncableWorldPos extends SyncableINBTCompound<WorldPos> {
    public SyncableWorldPos(String key, SidedWorldPos defVal) {
        super(key, defVal);
    }
}
