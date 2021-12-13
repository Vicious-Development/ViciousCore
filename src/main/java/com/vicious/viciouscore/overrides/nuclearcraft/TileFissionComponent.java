package com.vicious.viciouscore.overrides.nuclearcraft;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.tile.INotifiable;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import nc.init.NCBlocks;
import net.minecraft.block.Block;

import java.util.List;

public class TileFissionComponent extends TileMultiBlockComponent {
    public static List<Block> fissionBlockTiles = Lists.newArrayList(NCBlocks.fission_block,NCBlocks.cell_block,NCBlocks.ingot_block,NCBlocks.cooler,NCBlocks.reactor_door,NCBlocks.reactor_trapdoor,NCBlocks.reactor_casing_transparent);

    @Override
    public void addParent(INotifiable<Object> parent) {
        super.addParent(parent);
        notifyNeighbors();
    }
}
