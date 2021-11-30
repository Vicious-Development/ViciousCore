package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class VTileEntityRegistry {
    public static void register(){
        GameRegistry.registerTileEntity(TileMultiBlockComponent.class,"tile_multiblock_component");
    }
}
