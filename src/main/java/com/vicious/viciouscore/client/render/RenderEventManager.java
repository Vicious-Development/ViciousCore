package com.vicious.viciouscore.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class RenderEventManager {
    @SubscribeEvent
    public static void onHumanoidHeld(RenderLivingEvent.Pre<?> ev){
        handleRenderEntity(ev.getRenderer(), ev.getEntity());
    }
    @SubscribeEvent
    public static void onPlayerHeld(RenderPlayerEvent.Pre ev){
        handleRenderEntity(ev.getRenderer(),ev.getEntityLiving());
    }
    public static void handleRenderEntity(Render<?> renderer, EntityLivingBase entity){
        //Entity is compatible with our custom models.
        ItemStack itemHeld = entity.getHeldItemMainhand();
        //Is holding a render overrider, cancel that bitch.
        if(itemHeld.getItem() instanceof IRenderOverride) {
            ((IRenderOverride)itemHeld.getItem()).renderEntity(renderer,entity);
        }
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
        //TODO IMPL
    }

}
