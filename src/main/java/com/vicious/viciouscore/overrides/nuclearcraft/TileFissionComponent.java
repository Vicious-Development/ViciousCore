package com.vicious.viciouscore.overrides.nuclearcraft;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.tile.INotifiable;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import nc.init.NCBlocks;
import net.minecraft.block.Block;

import java.util.List;

public class TileFissionComponent extends TileMultiBlockComponent {
    public static List<Block> fissionBlockTiles = Lists.newArrayList(NCBlocks.fission_block,NCBlocks.cell_block,NCBlocks.ingot_block,NCBlocks.cooler,NCBlocks.reactor_door,NCBlocks.reactor_trapdoor,NCBlocks.reactor_casing_transparent);
    /*@Override
    public void notifyNeighbors() {
        forNeighborBlockTiles((bs,te,bpos)->{
            Block b = bs.getBlock();
            TileFissionComponent tfc = null;
            if(te instanceof TileFissionComponent){
                tfc = (TileFissionComponent) te;
            } else if(fissionBlockTiles.contains(b)){
                tfc = new TileFissionComponent();
                //This only works because there is a TileEntity Injector registered for the blocks I put in the list. (For any dev who may be seeing this).
                //This is here because the blocks won't create tiles on creation which is a no no.
                world.setTileEntity(bpos, tfc);
            }
            if(tfc != null){
                List<INotifiable<Object>> parentList = tfc.getParents();
                for (INotifiable<Object> parent : parentList) {
                    if(!parents.contains(parent)) parents.add(parent);
                }
                tfc.setParents(parents);
            }
        });
    }*/

    @Override
    public void addParent(INotifiable<Object> parent) {
        super.addParent(parent);
        notifyNeighbors();
    }
}
