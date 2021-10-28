package com.vicious.viciouscore.client.render.projectile;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.client.render.entity.RenderGenericEntity;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.common.entity.projectile.GenericModeledProjectile;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public abstract class RenderModeledProjectile<T extends GenericModeledProjectile> extends RenderGenericEntity<T> implements ICCModelConsumer {
    protected RenderModeledProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float partialticks) {
        super.doRender(entity,x,y,z,yaw,partialticks);

        //Get the current render state. Start drawing and bind our texture.
        CCRenderState rs = startAndBind(ResourceCache.ORBSPRITELOCATION);

        //Set the lighting values to the local light brightness (0-200).
        setLighting(ViciousRenderManager.getLightingBrightness(entity.getPosition()));

        //Get the matrix data for the camera distance.
        Matrix4 mat = getMatrix(x,y,z);

        //Apply animation modifications and render.
        getAnimation().runModelFrameAndRender(getModel(),x,y,z,yaw,partialticks,rs,mat);

        //Do this otherwise the client will crash. Crashing is bad.
        rs.draw();
    }
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(GenericModeledProjectile entity) {
        return null;
    }
}
