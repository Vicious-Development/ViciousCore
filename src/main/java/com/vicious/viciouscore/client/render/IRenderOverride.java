package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.client.render.entity.model.OverrideModelBiped;
import com.vicious.viciouscore.client.render.entity.model.RenderOverrideHandler;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.OverrideConfigurations;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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
    void registerRenderers();
    @SideOnly(Side.CLIENT)
    default void renderEntity(Render<?> renderer, EntityLivingBase e) {
        Item item = e.getHeldItemMainhand().getItem();
        //Changes how the entity renders while holding the item.
        if(renderer instanceof RenderBiped) {
            RenderLiving<?> entityRenderer = (RenderLiving<?>) renderer;
            OverrideModelBiped model = RenderOverrideHandler.overrideModelBiped((RenderBiped<?>) entityRenderer);
            OverrideConfigurations overridecfg = OverrideConfigurations.getConfiguration(item);
            if(overridecfg != null) {
                EntityModelOverride<ModelBiped> configurations = overridecfg.getEntityModelConfig(model);
                model.ignoreHandSides.add(EnumHandSide.RIGHT);
                model.transforms.offer(() -> {
                    model.applicate(configurations);
                });
            }
        }
    }
}
