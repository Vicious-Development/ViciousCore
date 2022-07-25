package com.vicious.viciouscore.common.tile;

import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.phantom.PhantomMemory;
import com.vicious.viciouscore.common.phantom.PhantomMemoryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


/**
 * A tile entity intended to allow data modification without loading the tile.
 * This allows for tiles to remain unloaded add still function.
 */
public class PhantomTE extends VCTE{
    protected final PhantomMemory data = setup();

    protected PhantomMemory setup(){
        return PhantomMemoryManager.getInstance().setup(PhantomMemory::new,worldPosition);
    }

    public PhantomTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public SyncableCompound getData() {
        return data.getData();
    }
}
