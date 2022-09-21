package com.vicious.viciouscore.common.capability.types.logistics.logistics;

import com.vicious.viciouscore.common.data.implementations.SyncableSidedWorldPos;
import com.vicious.viciouscore.common.util.server.ServerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class TileCapabilityConnection<T> extends CapabilityConnection<SyncableSidedWorldPos,T> {
    public TileCapabilityConnection(){
        this(new SyncableSidedWorldPos("pos",new SidedWorldPos(ServerHelper.getMainLevel(),new BlockPos(-1,-1,-1), Direction.DOWN)));
    }
    public TileCapabilityConnection(SyncableSidedWorldPos key) {
        super(key);

    }

    public TileCapabilityConnection(LazyOptional<T> optional, SidedWorldPos key) {
        super(optional, new SyncableSidedWorldPos("pos",key));
    }
}
