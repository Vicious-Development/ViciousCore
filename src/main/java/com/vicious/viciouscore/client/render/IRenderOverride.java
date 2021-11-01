package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.client.render.entity.model.OverrideModelBiped;
import com.vicious.viciouscore.client.render.entity.model.RenderOverrideHandler;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.RenderConfiguration;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRenderOverride extends ICCModelUser {
    /**
     * For when rendering on the client's screen while held.
     * @param e
     */
    @SideOnly(Side.CLIENT)
    default void renderClient(RenderSpecificHandEvent e){
        e.setCanceled(true);
    }

    /**
     * Cancels an event if the overlay it renders is unnecessary.
     * @param e
     */
    @SideOnly(Side.CLIENT)
    void cancelRenderOverlays(RenderGameOverlayEvent e);
    @SideOnly(Side.CLIENT)
    void renderEntity(Render<?> renderer, EntityLivingBase e);
    @SideOnly(Side.CLIENT)
    void registerRenderers();
}
