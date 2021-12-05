package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class VTileEntityRegistry {
    public static void register(){
        GameRegistry.registerTileEntity(TileMultiBlockComponent.class,new ResourceLocation(ViciousCore.MODID,"multiblock_component"));
    }
}
