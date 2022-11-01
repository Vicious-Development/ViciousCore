package com.vicious.viciouscore.overrides;


import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrider;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import com.vicious.viciouscore.overrides.techreborn.OverrideCentrifugeRecipeHandler;
import com.vicious.viciouscore.overrides.techreborn.OverrideTileFusionControlComputer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;

public class VCoreOverrides {
    public static void init(){
        ViciousCoreOverrideConfig cfg = ViciousCoreOverrideConfig.getInstance();
        String modid = ViciousCore.MODID;
        String targetModid = "techreborn";
        int priority = 9000 + 1;
        if(Loader.isModLoaded(targetModid)) {
            if (cfg.centrifuge.getBoolean()) OverrideHandler.registerInitInjector(
                    new FieldRetrievalRoute("techreborn.api.recipe.Recipes", "centrifuge"),
                    OverrideCentrifugeRecipeHandler::new);
            if (cfg.fusion.getBoolean()) {
                GameRegistry.registerTileEntity(OverrideTileFusionControlComputer.class, new ResourceLocation("techreborn", "fusion_control_computer"));
                //Doing this prevents the game from getting unnecessarily angry.
                GameRegistry.registerTileEntity(TileFusionControlComputer.class, new ResourceLocation("techreborn", "fusion_control_computer_old"));
                TileEntityOverrideHandler.registerOverrider("techreborn.tiles.fusionReactor.TileFusionControlComputer", new TileEntityOverrider(OverrideTileFusionControlComputer::new, modid, targetModid, priority));
            }
        }
        //Yabba registers its tiles on the block registry event so we need to do so after it does.
        targetModid = "yabba";
        SpecialOverrideHandler.doYabba=Loader.isModLoaded(targetModid) && cfg.yabba.getBoolean();
        if(SpecialOverrideHandler.doYabba){
            MinecraftForge.EVENT_BUS.register(SpecialOverrideHandler.class);
        }

        /*targetModid = "nuclearcraft";
        if(Loader.isModLoaded(targetModid)){
            //Checks if we are dealing with NCOverhauled or NCPreOverhaul. We want the pre.
            if(Global.MOD_NAME.length() == "NuclearCraft".length()){ //Lies intellij, lies
                //Only need to override the new fission controller.
                if(cfg.ncfission.getBoolean()) {
                    GameRegistry.registerTileEntity(OverrideTileFissionControllerNew.class, new ResourceLocation("nuclearcraft", "fission_controller_new"));
                    GameRegistry.registerTileEntity(TileFissionController.New.class, new ResourceLocation("nuclearcraft", "fission_controller_new_old"));
                    TileEntityOverrideHandler.registerOverrider("nc.tile.generator.TileFissionController$New", new TileEntityOverrider(OverrideTileFissionControllerNew::new, modid, targetModid, priority));

                    GameRegistry.registerTileEntity(OverrideTileActiveCooler.class, new ResourceLocation("nuclearcraft", "active_cooler"));
                    GameRegistry.registerTileEntity(TileActiveCooler.class, new ResourceLocation("nuclearcraft", "active_cooler_old"));
                    TileEntityOverrideHandler.registerOverrider("nc.tile.fluid.TileActiveCooler", new TileEntityOverrider(OverrideTileActiveCooler::new, modid, targetModid, priority));

                    GameRegistry.registerTileEntity(OverrideTileBuffer.class, new ResourceLocation("nuclearcraft", "buffer"));
                    GameRegistry.registerTileEntity(TileBuffer.class, new ResourceLocation("nuclearcraft", "buffer_old"));
                    TileEntityOverrideHandler.registerOverrider("nc.tile.energyFluid.TileBuffer", new TileEntityOverrider(OverrideTileBuffer::new, modid, targetModid, priority));

                    GameRegistry.registerTileEntity(TileFissionComponent.class, new ResourceLocation("viciouscore", "tile_fission_component"));
                    BlockOverrideHandler.registerTileEntity(NCBlocks.fission_block, TileFissionComponent::new);
                    BlockOverrideHandler.registerTileEntity(NCBlocks.reactor_casing_transparent, TileFissionComponent::new);
                    BlockOverrideHandler.registerTileEntity(NCBlocks.cooler, TileFissionComponent::new);
                    BlockOverrideHandler.registerTileEntity(NCBlocks.cell_block, TileFissionComponent::new);
                    //This is a little generic but it will cover fission moderators so oh well.
                    BlockOverrideHandler.registerTileEntity(NCBlocks.ingot_block, TileFissionComponent::new);
                }
            }
        }*/
    }
    //RegistryNamespaced< ResourceLocation, Class <? extends TileEntity>> reg = (RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>>) Reflection.accessField(TileEntity.class,"REGISTRY");
}
