package com.vicious.viciouscore.common.block;

import com.vicious.viciouscore.common.tile.INotifier;
import com.vicious.viciouscore.common.tile.tickless.TileMultiBlockComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


/**
 * Block that notifies a parent TileEntity when updated.
 * All mods should do this, it reduces the need to recheck if the multiblock has been damaged.
 */
public class BlockMultiblockComponent extends VCBlock implements EntityBlock {
    public BlockMultiblockComponent(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockDestroyed(Level worldIn, BlockPos pos) {
        notifyParent(worldIn, pos);
        super.onBlockDestroyed(worldIn, pos);
    }

    @Override
    public void onBlockUpdated(Level worldIn, BlockPos pos) {
        //Disabled notify for now to allow more control.
        //notifyParent(worldIn, pos);
        super.onBlockUpdated(worldIn, pos);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        BlockEntity te = level.getBlockEntity(pos);
        if(te == null) return;
        te.getLevel();
        notifyParent(level,pos);
    }

    public void notifyParent(LevelReader worldin, BlockPos pos){
        BlockEntity tile = worldin.getBlockEntity(pos);
        if(tile instanceof INotifier){
            ((INotifier<?>)tile).notifyParent();
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new TileMultiBlockComponent(pos,blockState);
    }
}
