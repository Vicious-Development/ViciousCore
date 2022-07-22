package com.vicious.viciouscore.common.tile;

import com.vicious.viciouslib.util.TriConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ViciousTE extends BlockEntity {
    public ViciousTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public List<BlockEntity> getNeighborBEntitys(){
        List<BlockEntity> list = new ArrayList<>();
        if(isChunkActive()) return list;
        list.add(level.getBlockEntity(worldPosition.offset(0,0,1)));
        list.add(level.getBlockEntity(worldPosition.offset(0,0,-1)));
        list.add(level.getBlockEntity(worldPosition.offset(0,1,0)));
        list.add(level.getBlockEntity(worldPosition.offset(0,-1,0)));
        list.add(level.getBlockEntity(worldPosition.offset(1,0,0)));
        list.add(level.getBlockEntity(worldPosition.offset(-1,0,0)));
        return list;
    }
    
    public boolean isChunkActive(){
        return true;
    }
    public List<BlockState> getNeighborBlocks() {
        List<BlockState> list = new ArrayList<>();
        if(isChunkActive()) return list;
        list.add(level.getBlockState(worldPosition.offset(0,0,1)));
        list.add(level.getBlockState(worldPosition.offset(0,0,-1)));
        list.add(level.getBlockState(worldPosition.offset(0,1,0)));
        list.add(level.getBlockState(worldPosition.offset(0,-1,0)));
        list.add(level.getBlockState(worldPosition.offset(1,0,0)));
        list.add(level.getBlockState(worldPosition.offset(-1,0,0)));
        return list;
    }
    public void forNeighborBlockTiles(TriConsumer<BlockState,BlockEntity,BlockPos> consumer){
        if(isChunkActive()) return;
        if(level == null) return;
        BlockPos p = worldPosition.offset(0,0,1);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        p = worldPosition.offset(0,0,-1);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        p = worldPosition.offset(0,1,0);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        p = worldPosition.offset(0,-1,0);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        p = worldPosition.offset(1,0,0);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        p = worldPosition.offset(-1,0,0);
        consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
    }
}
