package com.vicious.viciouscore.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class RenderEventManager {
    static float angle = 0;
    @SubscribeEvent
    public static void onHumanoidHeld(RenderLivingEvent.Pre<?> ev){
        EntityLivingBase entity = ev.getEntity();
        Render<?> render = ev.getRenderer();
        //Entity is compatible with our custom models.
        ItemStack itemHeld = entity.getHeldItemMainhand();
        //Is holding a render overrider, cancel that bitch.
        if(itemHeld.getItem() instanceof IRenderOverride) {
            ((IRenderOverride)itemHeld.getItem()).renderEntity(ev);
        }
        angle+=0.05;
    }
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent ev){
        Item held = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem();
        if(held instanceof IRenderOverride){
            ((IRenderOverride) held).cancelRenderOverlays(ev);
        }
    }
    @SubscribeEvent
    public static void onRenderBossOverlay(RenderGameOverlayEvent.BossInfo ev){
        new SPacketUpdateBossInfo().getUniqueId()
    }

}
