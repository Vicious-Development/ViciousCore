package com.vicious.viciouscore.common.tile.tickable;


import com.vicious.viciouscore.common.tile.VCTE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TETickable extends VCTE {
    public TETickable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public void tickServer() {}
    public void tickClient() {}
}
