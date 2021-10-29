package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.client.render.entity.model.RenderOverrideHandler;
import com.vicious.viciouscore.client.render.entity.model.OverrideModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;

public class ItemEnergoRifle extends ItemGun {
    public ItemEnergoRifle(String name){
        super(name);
    }
    @Override
    public void renderClient(RenderSpecificHandEvent e) {
        //e.getHand()
    }

    @Override
    public void renderEntity(Render<?> renderer, EntityLivingBase e) {
        //Change how the entity renders while holding the weapon.
        if(renderer instanceof RenderBiped) {
            RenderLiving<?> entityRenderer = (RenderLiving<?>) renderer;
            OverrideModelBiped model = RenderOverrideHandler.overrideModelBiped((RenderBiped<?>) entityRenderer);
            model.ignoreHandSides.add(EnumHandSide.RIGHT);
            model.transforms.offer(()->{
                model.bipedRightArm.rotateAngleX = 80;
            });
        }
    }
}
