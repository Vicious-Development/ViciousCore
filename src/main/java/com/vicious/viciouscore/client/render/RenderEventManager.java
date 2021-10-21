package com.vicious.viciouscore.client.render;

import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.vicious.viciouscore.client.render.animation.Animation;
import com.vicious.viciouscore.client.render.animation.CCModelFrameRunner;
import com.vicious.viciouscore.client.render.animation.TickableAnimation;
import com.vicious.viciouscore.common.item.ItemGun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class RenderEventManager {
    public final static TickableAnimation LOOKSIGHTSANIMATION = Animation.<TickableAnimation>newSingleFrame(
            new CCModelFrameRunner.VariableRotator(
                    RenderEventManager::getHeldGunSightTime,()->0D,()->0D,()->0D,()->0D,()->0D
            )
    );

    private static double getHeldGunSightTime() {
        Item gun = getHeldItem();
        if(!(gun instanceof ItemGun)) return 0;
        else{
            return ((ItemGun)gun).getSightTime();
        }
    }

    private static Item getHeldItem() {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getItem();
    }

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START) startTick(event);
        else if(event.phase == TickEvent.Phase.END) finishTick(event);
    }

    private void startTick(TickEvent.ClientTickEvent event) {
        updateLookSights(event);
    }

    private void updateLookSights(TickEvent.ClientTickEvent event) {

    }

    /**
     * Tells the client side whether or not the user is actually trying to and can interact with the world.
     * @return
     */
    private boolean isPlaying(){
        return Minecraft.getMinecraft().inGameHasFocus && !Minecraft.getMinecraft().player.isSpectator();
    }
    private boolean isCapableOfUsingSights()
    {
        //TODO: ADD CONFIG CONTROL. For now, right click.
        return isPlaying() && Mouse.isButtonDown(1);
    }
    private void finishTick(TickEvent.ClientTickEvent event) {
        EntityPlayer plr = Minecraft.getMinecraft().player;
        if(plr == null) return;
        ItemStack hand = plr.getHeldItemMainhand();
        //Checks if the item overrides rendering. If it does we need to override that bitch.
    }
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        EntityPlayer plr = Minecraft.getMinecraft().player;
        ItemStack hand = plr.getHeldItemMainhand();
        if(hand.getItem() instanceof IRenderOverride)
        {
            IRenderOverride overrider = (IRenderOverride) hand.getItem();
            //Cancel if the override render handles this stuff.
            overrider.cancelRenderOverlays(event);
        }
    }
    public void onRenderPlayerHeld(RenderItemEvent.Held event){

    }
    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event)
    {
        EntityPlayer plr = Minecraft.getMinecraft().player;
        ItemStack hand = plr.getHeldItemMainhand();
        if(hand.getItem() instanceof IRenderOverride)
        {
            IRenderOverride overrider = (IRenderOverride) hand.getItem();
            overrider.renderClient(event);
        }
    }
}
