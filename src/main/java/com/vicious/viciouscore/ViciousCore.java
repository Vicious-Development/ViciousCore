package com.vicious.viciouscore;

import com.vicious.viciouscore.client.ViciousCoreInputEventHandler;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.CommandConfig;
import com.vicious.viciouscore.common.commands.CommandStructure;
import com.vicious.viciouscore.common.event.ViciousCoreCommonEventHandler;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.packets.CMessageButtonPressReceived;
import com.vicious.viciouscore.common.network.packets.SMessageButtonUpdate;
import com.vicious.viciouscore.common.network.handlers.SButtonPressHandler;
import com.vicious.viciouscore.common.override.MobSpawnListener;
import com.vicious.viciouscore.common.override.OverrideHandler;
import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import com.vicious.viciouscore.common.override.block.SpongeEventHandler;
import com.vicious.viciouscore.common.override.tile.TileEntityOverrideHandler;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VTileEntityRegistry;
import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod("viciouscore")
public class ViciousCore
{
    private static int nextPacketId = -1;
    public static ViciousCTab TABVICIOUS = new ViciousCTab("viciouscreativetab", new ViciousItem("creativeicon", false));
    public boolean isFirstLoad(){
        return !CFG.firstLoad.getBoolean();
    }
    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.1.5";
    public static VCoreConfig CFG;
    public static ViciousCore instance;
    static {
        ViciousDirectories.initializeConfigDependents();
        CFG = VCoreConfig.getInstance();
    }

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();
        if(event.getSide() == Side.CLIENT) clientPreInit();
        if(isFirstLoad()) {
            logger.info("ViciousCore detected first load setup. Time to do some cool stuff and things!");
        }
        CommonKeyBindings.init();
        networkInit();
        VEntityRegistry.register();
        spongePreInit();
        MinecraftForge.EVENT_BUS.register(MobSpawnListener.class);
        MinecraftForge.EVENT_BUS.register(ViciousCorePlayerManager.class);
        MinecraftForge.EVENT_BUS.register(TileEntityOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockOverrideHandler.class);
        MinecraftForge.EVENT_BUS.register(ViciousCoreCommonEventHandler.class);
        VCoreOverrides.init();
        OverrideHandler.onPreInit();
        TileEntityOverrideHandler.init();
    }
    public void networkInit(){
        NETWORK.registerMessage(new SButtonPressHandler(), SMessageButtonUpdate.class,nextPacketId(), Side.SERVER);
        NETWORK.registerMessage(CButtonPressHandler.getInstance(), CMessageButtonPressReceived.class,nextPacketId(),Side.CLIENT);
    }
    public void spongePreInit(){
        if(!Loader.isModLoaded("sponge")) return;
        Sponge.getEventManager().registerListeners(this, new SpongeEventHandler());
    }
    public void clientPreInit(){
        MinecraftForge.EVENT_BUS.register(ViciousCoreInputEventHandler.class);
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
    private int nextPacketId(){
        nextPacketId++;
        return nextPacketId;
    }
}
