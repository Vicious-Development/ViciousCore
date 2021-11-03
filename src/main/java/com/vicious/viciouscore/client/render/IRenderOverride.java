package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.entity.model.RenderOverrideManager;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.OverrideConfigurations;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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
    void registerRenderers();
    @SideOnly(Side.CLIENT)
    default void renderEntity(Render<?> renderer, EntityLivingBase e) {
        Item item = e.getHeldItemMainhand().getItem();
        //Changes how the entity renders while holding the item.
        OverrideConfigurations overridecfg = ((IRenderOverride)item).getConfiguration();
        if(overridecfg == null) return;
        IOverrideModel model = RenderOverrideManager.overrideModel((RenderLivingBase<?>) renderer);
        EntityModelOverride<?> configurations = overridecfg.getEntityModelConfig(RenderOverrideManager.getRenderModel(renderer.getClass()));
        applicateConfiguration(model,configurations);
    }
    default void applicateConfiguration(IOverrideModel model, EntityModelOverride<?> configurations){
        if(model != null && configurations != null) {
            model.queueTransformer(() -> {
                model.applicate(configurations);
            });
        }
    }
    @SideOnly(Side.CLIENT)
    OverrideConfigurations getConfiguration();
}
