package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.common.data.values.Syncable;
import com.vicious.viciouscore.common.data.values.SyncablePrimitive;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Direction;

public abstract class SidedSyncableData extends SyncableData {
    public SyncablePrimitive<IntList> sidesPermitted = Syncable.alwaysDirty(new SyncablePrimitive<>(new IntArrayList(),"sides"));
    public SidedSyncableData(){
        super();
        track(sidesPermitted);
    }

    public boolean isAccessible(Direction side) {
        if(side == null) return sidesPermitted.get().contains(-1);
        return sidesPermitted.get().contains(side.ordinal());
    }
}
