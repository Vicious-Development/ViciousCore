package com.vicious.viciouscore;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ResourceUtils;
import com.vicious.viciouscore.client.configuration.HeldItemOverrideCFG;
import com.vicious.viciouscore.client.registries.RenderRegistry;
import com.vicious.viciouscore.client.render.RenderEventManager;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.client.util.ClientMappingsInitializer;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.CommandConfig;
import com.vicious.viciouscore.common.commands.CommandItemModelConfigReload;
import com.vicious.viciouscore.common.commands.CommandStructure;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.override.MobSpawnModifier;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.override.chunk.ChunkOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VTileEntityRegistry;
import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.tracking.VCTrackingHandler;
import com.vicious.viciouscore.overrides.VCoreOverrides;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, acceptableRemoteVersions = "*", dependencies = "required-after:codechickenlib;after:reborncore;after:techreborn;after:nuclearcraft")
public class ViciousCore
{
    public static ViciousCTab TABVICIOUS = new ViciousCTab("viciouscreativetab", new ViciousItem("creativeicon", false));

    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.0";
    public static VCoreConfig CFG;
    public static ViciousCore instance;
    static {
        VCTrackingHandler.init();
        Directories.initializeConfigDependents();
        CFG = VCoreConfig.init();
    }

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        if(!CFG.firstLoad.getBoolean()) {
            System.out.println("ViciousCore detected first load setup. Time to do some cool stuff and things!");
            if(event.getSide() == Side.CLIENT) HeldItemOverrideCFG.copyFromResources(MODID,this.getClass());
        }
        logger = event.getModLog();
        VEntityRegistry.register();
        if(event.getSide() == Side.CLIENT) {
            clientPreInit(event);
        }
        MinecraftForge.EVENT_BUS.register(MobSpawnModifier.class);
        MinecraftForge.EVENT_BUS.register(ViciousCorePlayerManager.class);
        MinecraftForge.EVENT_BUS.register(TileEntityOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(ChunkOverrideHandler.class);
        VCoreOverrides.init();
        OverrideHandler.onPreInit();
        TileEntityOverrideHandler.init();
    }

    /**
     * Proxies are stupid. This works completely fine.
     * @param event
     */
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent event){
        ClientMappingsInitializer.init();
        RenderRegistry.register();
        MinecraftForge.EVENT_BUS.register(RenderEventManager.class);
        ClientCommandHandler.instance.registerCommand(new CommandItemModelConfigReload());
        //Necessary CCL implementations
        TextureUtils.addIconRegister(new ResourceCache());
        ResourceUtils.registerReloadListener(new ResourceCache());
    }
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent event){
        new ViciousRenderManager(); //Instance is stored in the VRM.
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide() == Side.CLIENT) {
            clientInit(event);
        }
        if(!CFG.firstLoad.getBoolean()) CFG.firstLoad.set(true);
        VTileEntityRegistry.register();
        OverrideHandler.onInit();
    }

    @EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandConfig());
        event.registerServerCommand(new CommandStructure());
    }
    @EventHandler
    public void onStop(FMLModDisabledEvent event){
        CFG.save();
        HeldItemOverrideCFG.saveAll();
    }
}
