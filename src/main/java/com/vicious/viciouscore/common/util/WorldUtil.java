package com.vicious.viciouscore.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil{
    public static BlockPos getNextGround(World worldin, BlockPos initialPos){
        Chunk c = worldin.getChunkFromBlockCoords(initialPos);
        for (int y = 0; y < initialPos.getY(); y--) {
            IBlockState state =  c.getBlockState(initialPos.getX(),y,initialPos.getZ());
            if(state.getBlock().isCollidable()){
                return new BlockPos(initialPos.getX(),y,initialPos.getZ());
            }
        }
        return null;
    }

    public static BlockPos getNextChunkGround(World worldin, BlockPos initialPos) {
        Chunk c = worldin.getChunkFromBlockCoords(initialPos);
        for (int y = 0; y < initialPos.getY(); y--) {
            IBlockState state =  c.getBlockState(initialPos.getX()+8,y,initialPos.getZ()+8);
            if(state.getBlock().isCollidable()){
                return new BlockPos(initialPos.getX()+8,y,initialPos.getZ()+8);
            }
        }
        return null;
    }
}

