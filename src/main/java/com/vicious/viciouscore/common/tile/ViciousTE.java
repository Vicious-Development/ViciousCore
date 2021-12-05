package com.vicious.viciouscore.common.tile;

import com.vicious.viciouscore.common.override.chunk.ViciousChunk;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;

public class ViciousTE extends TileEntity {
    /**
     * ViciousCore overwrites chunks when they are properly loaded.
     */
    public boolean chunkIsCorrect() {
        if(world == null) return false;
        ChunkPos cpos = new ChunkPos(pos);
        if(!world.isChunkGeneratedAt(cpos.x, cpos.z)) return false;
        return world.getChunkFromChunkCoords(cpos.x,cpos.z) instanceof ViciousChunk;
    }
    public List<TileEntity> getNeighborTiles(){
        List<TileEntity> list = new ArrayList<>();
        list.add(world.getTileEntity(pos.add(0,0,1)));
        list.add(world.getTileEntity(pos.add(0,0,-1)));
        list.add(world.getTileEntity(pos.add(0,1,0)));
        list.add(world.getTileEntity(pos.add(0,-1,0)));
        list.add(world.getTileEntity(pos.add(1,0,0)));
        list.add(world.getTileEntity(pos.add(-1,0,0)));
        return list;
    }
    public List<IBlockState> getNeighborBlocks() {
        List<IBlockState> list = new ArrayList<>();
        list.add(world.getBlockState(pos.add(0,0,1)));
        list.add(world.getBlockState(pos.add(0,0,-1)));
        list.add(world.getBlockState(pos.add(0,1,0)));
        list.add(world.getBlockState(pos.add(0,-1,0)));
        list.add(world.getBlockState(pos.add(1,0,0)));
        list.add(world.getBlockState(pos.add(-1,0,0)));
        return list;
    }
    public void forNeighborBlockTiles(TriConsumer<IBlockState,TileEntity, BlockPos> consumer){
        if(world == null || pos == null) return;
        BlockPos p = pos.add(0,0,1);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
        p = pos.add(0,0,-1);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
        p = pos.add(0,1,0);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
        p = pos.add(0,-1,0);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
        p = pos.add(1,0,0);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
        p = pos.add(-1,0,0);
        consumer.accept(world.getBlockState(p),world.getTileEntity(p),p);
    }
}
