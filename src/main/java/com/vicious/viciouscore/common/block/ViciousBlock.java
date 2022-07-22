package com.vicious.viciouscore.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public class ViciousBlock extends Block {


    public ViciousBlock(Properties properties) {
        super(properties);
    }

    public void onBlockDestroyed(Level worldIn, BlockPos pos) {
    }
    public void onBlockUpdated(Level worldIn, BlockPos pos){

    }

    /*@Override
    public boolean onBlockActivated(Level worldIn, BlockPos pos, BlockState state, Player playerIn,  hand, Direction facing, float hitX, float hitY, float hitZ) {
        onBlockUpdated(worldIn,pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }*/

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        boolean b = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        if(b) onBlockDestroyed(level,pos);
        return b;
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handSide, BlockHitResult hitResult) {
        onBlockUpdated(level,pos);
        return super.use(state, level, pos, player, handSide, hitResult);
    }
    /*@Override
    public void onBlockClicked(Level worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        onBlockUpdated(worldIn,pos);
    }*/


    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        onBlockDestroyed(level,pos);
    }
}
