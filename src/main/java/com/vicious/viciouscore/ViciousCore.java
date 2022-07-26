package com.vicious.viciouscore;

import com.vicious.viciouscore.client.ViciousCoreInputEventHandler;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.phantom.PhantomMemoryManager;
import com.vicious.viciouscore.common.tile.VCBlockEntities;
import com.vicious.viciouscore.common.util.ServerHelper;
import com.vicious.viciouscore.common.util.SidedExecutor;
import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("viciouscore")
public class ViciousCore
{
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
    public ViciousCore(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onStop);
        SidedExecutor.clientOnly(()->{
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        });
    }

    public static final Logger logger = LogManager.getLogger();
    public void setup(FMLCommonSetupEvent event)
    {
        instance = this;
        if(isFirstLoad()) {
            logger.info("ViciousCore detected first load setup. Time to do some cool stuff and things!");
        }
        CommonKeyBindings.setup();
        //Initialize the network.
        VCNetwork.getInstance();
        MinecraftForge.EVENT_BUS.register(VCCapabilities.class);
        MinecraftForge.EVENT_BUS.register(CommonKeyBindings.class);
        MinecraftForge.EVENT_BUS.register(PhantomMemoryManager.class);
        MinecraftForge.EVENT_BUS.register(ServerHelper.class);
        VCBlockEntities.init();
    }
    public void networkInit(){
        VCNetwork.getInstance();
    }
    public void clientSetup(FMLClientSetupEvent event){
        MinecraftForge.EVENT_BUS.register(ViciousCoreInputEventHandler.class);
    }
    public void postInit(FMLLoadCompleteEvent event)
    {
        if(isFirstLoad()) {
            CFG.firstLoad.set(true);
        }
    }

    public void serverInit(ServerStartingEvent event) {
        //TODO: commands
        //event.getServer().com(new CommandConfig());
        //event.registerServerCommand(new CommandStructure());
    }
    public void onStop(GameShuttingDownEvent event){
        CFG.save();
    }
}
