package com.vicious.viciouscore.client.render.living;

import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.client.render.ICCModelUser;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;

public abstract class RenderEntityLiving<T extends EntityLiving> extends RenderLiving<T> implements ICCModelUser, ICCModelConsumer {
    public RenderEntityLiving(RenderManager in){
        super(in, new ModelPlayer(1f,true), 1F);
    }

    public RenderEntityLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }


    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks){
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}