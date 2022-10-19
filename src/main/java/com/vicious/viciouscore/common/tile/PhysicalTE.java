package com.vicious.viciouscore.common.tile;


import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.Editable;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Opposite of a PhantomTile, requires the chunk to be loaded to function.
 */
public class PhysicalTE extends VCTE{
    //Allow editing the compound so that its internal components are reachable.
    @Editable
    public SyncableCompound data = new SyncableCompound("vcdat");
    public PhysicalTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public SyncableCompound getData() {
        return data;
    }
}
