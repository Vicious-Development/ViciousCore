package com.vicious.viciouscore.overrides;

import com.latmod.yabba.tile.TileItemBarrelConnector;
import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrider;
import com.vicious.viciouscore.overrides.yabba.OverrideTileItemBarrelConnector;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SpecialOverrideHandler {
    public static boolean doYabba = false;
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if(doYabba){
            GameRegistry.registerTileEntity(OverrideTileItemBarrelConnector.class, new ResourceLocation("yabba", "item_barrel_connector"));
            GameRegistry.registerTileEntity(TileItemBarrelConnector.class, new ResourceLocation("yabba", "item_barrel_connector_old"));
            TileEntityOverrideHandler.registerOverrider("com.latmod.yabba.tile.TileItemBarrelConnector", new TileEntityOverrider(OverrideTileItemBarrelConnector::new, ViciousCore.MODID, "yabba", 9001));
        }
    }
}
