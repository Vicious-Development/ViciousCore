package com.vicious.viciouscore.common.override.chunk;

import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.util.reflect.IFieldCloner;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Map;

/**
 * Extremely cursed. Needed to allow support for TE injection
 */
public class ViciousChunk extends Chunk implements IFieldCloner {
    public World world;
    public ViciousChunk(Object og){
        super(((Chunk)og).getWorld(),((Chunk)og).x,((Chunk)og).z);
        clone(og);
    }
    public ViciousChunk(World worldIn, int x, int z) {
        super(worldIn, x, z);
    }

    public ViciousChunk(World worldIn, ChunkPrimer primer, int x, int z) {
        super(worldIn, primer, x, z);
    }
    @SuppressWarnings("unchecked")
    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
    {
        World world = getWorld();
        if (tileEntityIn.getWorld() != world) //Forge don't call unless it's changed, could screw up bad mods.
            tileEntityIn.setWorld(world);
        tileEntityIn.setPos(pos);

        Block b = this.getBlockState(pos).getBlock();
        if (b.hasTileEntity(this.getBlockState(pos)) || BlockOverrideHandler.hasTileInjector(b))
        {
            Map<BlockPos,TileEntity> tileEntities = getTileEntityMap();
            if (tileEntities.containsKey(pos)) {
                (tileEntities.get(pos)).invalidate();
            }
            //Swapped these two around to allow for tiles to detect create other tiles without cause SORs.
            //I don't know if there's negative consequences so I'll leave a note here: FIX
            tileEntities.put(pos, tileEntityIn);
            tileEntityIn.validate();
        }
    }
}
