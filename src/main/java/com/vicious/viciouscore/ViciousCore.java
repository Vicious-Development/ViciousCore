package com.vicious.viciouscore;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ResourceUtils;
import com.vicious.viciouscore.client.registries.RenderRegistry;
import com.vicious.viciouscore.client.render.RenderEventManager;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.client.render.item.configuration.OverrideConfigurations;
import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.ViciousCTab;
import com.vicious.viciouscore.common.commands.ConfigCommand;
import com.vicious.viciouscore.common.commands.ItemModelConfigReloadCommand;
import com.vicious.viciouscore.common.item.ViciousItem;
import com.vicious.viciouscore.common.modification.MobSpawnModifier;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.registries.VItemRegistry;
import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.file.FileUtil;
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
import org.lwjgl.Sys;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, dependencies = "after:codechickenlib")
public class ViciousCore
{
    public static final ViciousCTab TABVICIOUS = new ViciousCTab("viciouscreativetab", new ViciousItem("creativeicon", false));

    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.0";
    public static VCoreConfig CFG;
    public static ViciousCore instance;

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        Directories.initializeConfigDependents();
        CFG = VCoreConfig.init();
        if(!CFG.firstLoad.getBoolean()) {
            System.out.println("ViciousCore detected first load setup. Time to do some cool stuff and things!");
            if(event.getSide() == Side.CLIENT) OverrideConfigurations.copyFromResources(MODID,this.getClass());
        }
        logger = event.getModLog();
        System.out.println("INIT STARTED");
        VEntityRegistry.register();
        if(event.getSide() == Side.CLIENT) {
            clientPreInit(event);
        }
        MinecraftForge.EVENT_BUS.register(MobSpawnModifier.class);
        System.out.println("INIT FINISHED");
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
        RenderRegistry.register();
        //Necessary CCL implementations
        MinecraftForge.EVENT_BUS.register(RenderEventManager.class);
        TextureUtils.addIconRegister(new ResourceCache());
        ResourceUtils.registerReloadListener(new ResourceCache());
        ClientCommandHandler.instance.registerCommand(new ItemModelConfigReloadCommand());
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
    }

    @EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        event.registerServerCommand(new ConfigCommand());
    }
    @EventHandler
    public void onStop(FMLModDisabledEvent event){
        CFG.save();
        OverrideConfigurations.saveAll();
    }
}
