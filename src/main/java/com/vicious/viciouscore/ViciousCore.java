package com.vicious.viciouscore;

import com.vicious.viciouscore.aunotamation.Aunotamations;
import com.vicious.viciouscore.client.ViciousCoreInputEventHandler;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.capability.CapabilityEventHandler;
import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.events.Ticker;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.util.SidedExecutor;
import com.vicious.viciouscore.common.util.file.ViciousDirectories;
import com.vicious.viciouscore.common.util.server.ServerHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    public static final String MODID = "viciouscore";
    public static VCoreConfig CFG;
    public static ViciousCore instance;

    public ViciousCore(){
        Aunotamations.init();
        ViciousDirectories.initializeConfigDependents();
        CFG = VCoreConfig.getInstance();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(VCCapabilities::onCapRegistry);
        SidedExecutor.clientOnly(()->{
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        });
    }

    public static final Logger logger = LogManager.getLogger();
    public void setup(FMLCommonSetupEvent event)
    {
        instance = this;
        //Initialize the network.
        VCNetwork.getInstance();
        MinecraftForge.EVENT_BUS.register(ViciousCore.class);
        MinecraftForge.EVENT_BUS.register(CapabilityEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ServerHelper.class);
        MinecraftForge.EVENT_BUS.register(Ticker.class);
    }
    public void clientSetup(FMLClientSetupEvent event){
        CommonKeyBindings.register();
        MinecraftForge.EVENT_BUS.register(ViciousCoreInputEventHandler.class);
    }


    public void postInit(FMLLoadCompleteEvent event)
    {
        CFG.save();
    }

    @SubscribeEvent
    public void onStop(ServerStoppingEvent event){
        CFG.save();
    }
}
