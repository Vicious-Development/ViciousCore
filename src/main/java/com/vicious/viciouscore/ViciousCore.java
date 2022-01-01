package com.vicious.viciouscore;

import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.CommandConfig;
import com.vicious.viciouscore.common.commands.CommandStructure;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.override.MobSpawnListener;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.override.block.SpongeEventHandler;
import com.vicious.viciouscore.common.override.chunk.ChunkOverrideHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VTileEntityRegistry;
import com.vicious.viciouscore.common.util.file.Directories;
import com.vicious.viciouscore.overrides.VCoreOverrides;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, acceptableRemoteVersions = "*", dependencies = "after:reborncore;after:techreborn;after:nuclearcraft;after:sponge")
public class ViciousCore
{
    public static ViciousCTab TABVICIOUS = new ViciousCTab("viciouscreativetab", new ViciousItem("creativeicon", false));
    public boolean isFirstLoad(){
        return !CFG.firstLoad.getBoolean();
    }
    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.0.8";
    public static VCoreConfig CFG;
    public static ViciousCore instance;
    static {
        Directories.initializeConfigDependents();
        CFG = VCoreConfig.getInstance();
    }

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();
        if(isFirstLoad()) {
            logger.info("ViciousCore detected first load setup. Time to do some cool stuff and things!");
        }
        VEntityRegistry.register();
        spongePreInit();
        MinecraftForge.EVENT_BUS.register(MobSpawnListener.class);
        MinecraftForge.EVENT_BUS.register(ViciousCorePlayerManager.class);
        MinecraftForge.EVENT_BUS.register(TileEntityOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(ChunkOverrideHandler.class);
        VCoreOverrides.init();
        OverrideHandler.onPreInit();
        TileEntityOverrideHandler.init();
    }
    public void spongePreInit(){
        if(!Loader.isModLoaded("sponge")) return;
        Sponge.getEventManager().registerListeners(this, new SpongeEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(isFirstLoad()) {
            CFG.firstLoad.set(true);
        }
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
    }
}
