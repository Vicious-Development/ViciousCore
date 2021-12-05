package com.vicious.viciouscore.overrides;


import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrider;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.overrides.nuclearcraft.OverrideTileActiveCooler;
import com.vicious.viciouscore.overrides.nuclearcraft.OverrideTileFissionControllerNew;
import com.vicious.viciouscore.overrides.nuclearcraft.TileFissionComponent;
import com.vicious.viciouscore.overrides.techreborn.OverrideCentrifugeRecipeHandler;
import com.vicious.viciouscore.overrides.techreborn.OverrideTileFusionControlComputer;
import nc.Global;
import nc.init.NCBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class VCoreOverrides {
    public static void init(){
        String modid = ViciousCore.MODID;
        String targetModid = "techreborn";
        int priority = 9000 + 1;
        if(Loader.isModLoaded(targetModid)){
            OverrideHandler.registerInitInjector(
                    new FieldRetrievalRoute("techreborn.api.recipe.Recipes", "centrifuge"),
                    OverrideCentrifugeRecipeHandler::new);
            GameRegistry.registerTileEntity(OverrideTileFusionControlComputer.class, new ResourceLocation("techreborn","fusion_control_computer"));
            TileEntityOverrideHandler.registerOverrider("techreborn.tiles.fusionReactor.TileFusionControlComputer", new TileEntityOverrider(OverrideTileFusionControlComputer::new,modid,targetModid,priority));
        }
        targetModid = "nuclearcraft";
        if(Loader.isModLoaded("nuclearcraft")){
            //Checks if we are dealing with NCOverhauled or NCPreOverhaul. We want the pre.
            if(Global.MOD_NAME.length() == "NuclearCraft".length()){
                //Only need to override the new fission controller.
                GameRegistry.registerTileEntity(OverrideTileFissionControllerNew.class, new ResourceLocation("nuclearcraft","fission_controller_new"));
                TileEntityOverrideHandler.registerOverrider("nc.tile.generator.TileFissionController$New", new TileEntityOverrider(OverrideTileFissionControllerNew::new,modid,targetModid,priority));
                GameRegistry.registerTileEntity(OverrideTileActiveCooler.class, new ResourceLocation("nuclearcraft","active_cooler"));
                TileEntityOverrideHandler.registerOverrider("nc.tile.fluid.TileActiveCooler", new TileEntityOverrider(OverrideTileActiveCooler::new,modid,targetModid,priority));
                GameRegistry.registerTileEntity(TileFissionComponent.class,new ResourceLocation("viciouscore","tile_fission_component"));
                BlockOverrideHandler.registerTileEntity(NCBlocks.fission_block, TileFissionComponent::new);
                BlockOverrideHandler.registerTileEntity(NCBlocks.reactor_casing_transparent, TileFissionComponent::new);
                BlockOverrideHandler.registerTileEntity(NCBlocks.cooler, TileFissionComponent::new);
                BlockOverrideHandler.registerTileEntity(NCBlocks.cell_block, TileFissionComponent::new);
                //This is a little generic but it will cover fission moderators so oh well.
                BlockOverrideHandler.registerTileEntity(NCBlocks.ingot_block, TileFissionComponent::new);
            }
        }
    }
}
