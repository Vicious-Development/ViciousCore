package com.vicious.viciouscore.client.render.entity;

import com.vicious.viciouscore.client.render.ICCModelUser;
import com.vicious.viciouscore.common.entity.GenericEntity;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderGenericEntity<T extends GenericEntity> extends Render<T> implements ICCModelUser {
    protected RenderGenericEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks){
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
