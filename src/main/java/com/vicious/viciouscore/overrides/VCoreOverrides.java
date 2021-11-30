package com.vicious.viciouscore.overrides;


import com.vicious.viciouscore.common.override.BlockOverrider;
import com.vicious.viciouscore.common.override.Overrider;
import com.vicious.viciouscore.common.override.TileEntityOverrider;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.overrides.nuclearcraft.OverrideTileFissionControllerNew;
import com.vicious.viciouscore.overrides.techreborn.OverrideCentrifugeRecipeHandler;
import com.vicious.viciouscore.overrides.techreborn.OverrideTileFusionControlComputer;
import nc.Global;
import nc.init.NCBlocks;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class VCoreOverrides {
    public static void init(){
        if(Loader.isModLoaded("techreborn")){
            Overrider.registerInitInjector(
                    new FieldRetrievalRoute("techreborn.api.recipe.Recipes", "centrifuge"),
                    OverrideCentrifugeRecipeHandler::new);
            GameRegistry.registerTileEntity(OverrideTileFusionControlComputer.class,"fusion_control_computer");
            TileEntityOverrider.registerOverrider("techreborn.tiles.fusionReactor.TileFusionControlComputer", OverrideTileFusionControlComputer::new,"techreborn");
        }
        if(Loader.isModLoaded("nuclearcraft")){
            //Checks if we are dealing with NCOverhauled or NCPreOverhaul. We want the pre.
            if(Global.MOD_NAME.length() == "NuclearCraft".length()){
                //Only need to override the new fission controller.
                GameRegistry.registerTileEntity(OverrideTileFissionControllerNew.class,"fission_controller_new");
                TileEntityOverrider.registerOverrider("nc.tile.generator.TileFissionController.New", OverrideTileFissionControllerNew::new,"nuclearcraft");
                BlockOverrider.registerTileEntity(NCBlocks.fission_block, TileMultiBlockComponent::new);
                BlockOverrider.registerTileEntity(NCBlocks.reactor_casing_transparent, TileMultiBlockComponent::new);
                BlockOverrider.registerTileEntity(NCBlocks.cooler, TileMultiBlockComponent::new);
                BlockOverrider.registerTileEntity(NCBlocks.cell_block, TileMultiBlockComponent::new);
                //This is a little generic but it will cover fission moderators so oh well.
                BlockOverrider.registerTileEntity(NCBlocks.ingot_block, TileMultiBlockComponent::new);
            }
        }
    }
}
