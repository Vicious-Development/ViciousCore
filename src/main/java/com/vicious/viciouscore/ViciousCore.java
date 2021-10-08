package com.vicious.viciouscore;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ResourceUtils;
import com.vicious.viciouscore.client.registries.RenderRegistry;
import com.vicious.viciouscore.client.rendering.ViciousRenderManager;
import com.vicious.viciouscore.common.registries.VEntityRegistry;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = ViciousCore.MODID, name = ViciousCore.NAME, version = ViciousCore.VERSION, dependencies = "after:codechickenlib")
public class ViciousCore
{
    public static final String MODID = "viciouscore";
    public static final String NAME = "Vicious Core";
    public static final String VERSION = "1.0";
    public static ViciousCore instance;

    public static Logger logger;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        logger = event.getModLog();
        System.out.println("INIT STARTED");
        VEntityRegistry.register();
        if(event.getSide() == Side.CLIENT) {
            clientPreInit(event);
        }
        System.out.println("INIT FINISHED");
    }
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent event){
        RenderRegistry.register();
        //Necessary CCL implementations
        TextureUtils.addIconRegister(new ResourceCache());
        ResourceUtils.registerReloadListener(new ResourceCache());
    }
    public void clientInit(FMLInitializationEvent event){
        new ViciousRenderManager(); //Instance is stored in the VRM.
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide() == Side.CLIENT) {
            clientInit(event);
        }
    }
}
