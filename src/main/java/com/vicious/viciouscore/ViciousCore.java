package com.vicious.viciouscore;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ResourceUtils;
import com.vicious.viciouscore.client.registries.RenderRegistry;
import com.vicious.viciouscore.client.render.RenderEventManager;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.client.configuration.HeldItemOverrideCFG;
import com.vicious.viciouscore.client.util.ClientMappingsInitializer;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.ConfigCommand;
import com.vicious.viciouscore.common.commands.ItemModelConfigReloadCommand;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.override.MobSpawnModifier;
import com.vicious.viciouscore.common.override.Overrider;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VItemRegistry;
import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.tracking.VCTrackingHandler;
import com.vicious.viciouscore.overrides.VCoreOverrides;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, dependencies = "required-after:codechickenlib;after:reborncore;after:techreborn")
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
        VCoreOverrides.init();
        Overrider.onPreInit();
    }
    @SubscribeEvent
    public void itemInit(RegistryEvent.Register<Item> ev){
        VItemRegistry.register(ev);
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
        ClientCommandHandler.instance.registerCommand(new ItemModelConfigReloadCommand());
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
        Overrider.onInit();
    }

    @EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new ConfigCommand());
    }
    @EventHandler
    public void onStop(FMLModDisabledEvent event){
        CFG.save();
        HeldItemOverrideCFG.saveAll();
    }
}
