package com.vicious.viciouscore.common.tile;

import com.vicious.viciouscore.ViciousCoreLoadingPlugin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;

public class ViciousTE extends TileEntity {
    public List<TileEntity> getNeighborTiles(){
        List<TileEntity> list = new ArrayList<>();
        //if(isChunkActive()) return list;
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
        //if(isChunkActive()) return list;
        list.add(world.getBlockState(pos.add(0,0,1)));
        list.add(world.getBlockState(pos.add(0,0,-1)));
        list.add(world.getBlockState(pos.add(0,1,0)));
        list.add(world.getBlockState(pos.add(0,-1,0)));
        list.add(world.getBlockState(pos.add(1,0,0)));
        list.add(world.getBlockState(pos.add(-1,0,0)));
        return list;
    }
    public void forNeighborBlockTiles(TriConsumer<IBlockState,TileEntity,BlockPos> consumer){
        //if(isChunkActive()) return;
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
