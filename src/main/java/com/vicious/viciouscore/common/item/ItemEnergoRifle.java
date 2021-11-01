package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.client.render.entity.model.OverrideModelBiped;
import com.vicious.viciouscore.client.render.entity.model.RenderOverrideHandler;
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


}
