package com.vicious.viciouscore.client.rendering;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import com.vicious.viciouscore.client.rendering.animation.Animation;
import com.vicious.viciouscore.client.rendering.animation.CCModelFrameRunner;
import com.vicious.viciouscore.common.entity.projectile.GenericModeledProjectile;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public abstract class RenderModeledProjectile<T extends GenericModeledProjectile> extends GenericRenderableEntity<T> {
    //A scale of 1 meter.
    protected Scale scale = new Scale(0.5,0.5,0.5);
    protected static Animation anim = new Animation(450);
    static{
        anim.addFrameInRange(0,450,new CCModelFrameRunner.VariableRotator(
                ()->0.1,
                ()->0.1,
                ()->0.1,
                ()-> 1.0,
                ()-> 1.0,
                ()-> 1.0
        ));
    }
    protected RenderModeledProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float partialticks) {
        super.doRender(entity,x,y,z,yaw,partialticks);
        CCRenderState rs = CCRenderState.instance();
        setLighting(ViciousRenderManager.getLightingBrightness(entity.getPosition()));
        ViciousRenderManager.bindTexture(ResourceCache.ORBSPRITELOCATION);
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        Matrix4 mat = RenderUtils.getMatrix(new Vector3(x,y,z), new Rotation(0,0,0,1), 1).apply(this.scale);
        anim.runModelFrameAndRender(getModel(),x,y,z,yaw,partialticks,rs,mat);
        rs.draw();
    }

    public abstract CCModel getModel();
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(GenericModeledProjectile entity) {
        return null;
    }
    /*
    if(frame%100 < 50) {
            float frameFactor = 1F+(frame%100)/100F;
            scale = new Scale(0.5*frameFactor,0.5*frameFactor,0.5*frameFactor);
        }
        else{
            float frameFactorInv = 2F-(frame%100)/100F;
            scale = new Scale(0.5*frameFactorInv,0.5*frameFactorInv,0.5*frameFactorInv);
        }
     */
}
