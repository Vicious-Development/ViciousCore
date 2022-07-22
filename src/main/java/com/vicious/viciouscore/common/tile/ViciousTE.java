package com.vicious.viciouscore.common.tile;

import com.mojang.datafixers.types.Type;
import net.minecraft.block.state.IBlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;
import org.spongepowered.common.bridge.world.chunk.ActiveChunkReferantBridge;
import org.spongepowered.common.bridge.world.chunk.ChunkBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViciousTE extends TileEntity {
    public List<TileEntity> getNeighborTiles(){
        List<TileEntity> list = new ArrayList<>();
        if(isChunkActive()) return list;
        list.add(world.getTileEntity(pos.add(0,0,1)));
        list.add(world.getTileEntity(pos.add(0,0,-1)));
        list.add(world.getTileEntity(pos.add(0,1,0)));
        list.add(world.getTileEntity(pos.add(0,-1,0)));
        list.add(world.getTileEntity(pos.add(1,0,0)));
        list.add(world.getTileEntity(pos.add(-1,0,0)));
        return list;
    }

    /**
     * Sponge uses a chunk mixin that overrides something I implemented in VC chunk.
     * This is more of a temporary fix as VC chunk might interfere with other mods (although I'm not totally aware of any yet).
     */
    public boolean isChunkActive(){
        if(!ViciousCoreLoadingPlugin.isSpongeLoaded) return false;
        Chunk c = (Chunk) ((ActiveChunkReferantBridge)this).bridge$getActiveChunk();
        if(c == null) return true;
        return !((ChunkBridge)c).bridge$isActive();
    }
    public List<IBlockState> getNeighborBlocks() {
        List<IBlockState> list = new ArrayList<>();
        if(isChunkActive()) return list;
        list.add(world.getBlockState(pos.add(0,0,1)));
        list.add(world.getBlockState(pos.add(0,0,-1)));
        list.add(world.getBlockState(pos.add(0,1,0)));
        list.add(world.getBlockState(pos.add(0,-1,0)));
        list.add(world.getBlockState(pos.add(1,0,0)));
        list.add(world.getBlockState(pos.add(-1,0,0)));
        return list;
    }
    public void forNeighborBlockTiles(TriConsumer<IBlockState,TileEntity,BlockPos> consumer){
        if(isChunkActive()) return;
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
