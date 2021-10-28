package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.client.render.model.ModelOverrideManager;
import com.vicious.viciouscore.client.render.model.OverrideModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;

public class ItemEnergoRifle extends ItemGun {

    @Override
    public void renderClient(RenderSpecificHandEvent e) {
        //e.getHand()
    }

    @Override
    public void renderEntity(RenderLivingEvent<?> e) {
        //Change how the entity renders while holding the weapon.
        if(e.getRenderer() instanceof RenderBiped) {
            RenderLiving<?> entityRenderer = (RenderLiving<?>) e.getRenderer();
            OverrideModelBiped model = ModelOverrideManager.overrideBiped((RenderBiped<?>) entityRenderer);
            model.transforms.offer(()->{

            });
        }
    }
}
