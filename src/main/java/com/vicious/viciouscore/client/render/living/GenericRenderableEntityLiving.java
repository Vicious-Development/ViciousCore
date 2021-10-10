package com.vicious.viciouscore.client.render.living;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import com.vicious.viciouscore.client.render.animation.Animation;
import com.vicious.viciouscore.common.entity.GenericEntity;
import com.vicious.viciouscore.common.entity.projectile.GenericModeledProjectile;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class GenericRenderableEntityLiving <T extends EntityLiving> extends RenderLiving<T> {
    protected static Scale scale = new Scale(0.5,0.5,0.5);
    public GenericRenderableEntityLiving(RenderManager in){
        super(in, new ModelPlayer(1f,true), 1F);
    }

    public GenericRenderableEntityLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }


    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks){
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    protected void setLighting(float brightness){
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);
    }
    /**
     * Override to provide your own matrix.
     * @param x Camera x dist
     * @param y Camera y dist
     * @param z Camera z dist
     * @return Matrix with no rotation and of supplied scale
     */

    protected Matrix4 getMatrix(double x, double y, double z){
        //Create the default rendering matrix (no rotation, scale of 1 block wide.
        return RenderUtils.getMatrix(new Vector3(x,y,z), new Rotation(0,0,0,1), 1).apply(getScale());
    }
    protected Matrix4 getMatrix(Vector3 vec, Rotation rot){
        //Create the default rendering matrix (no rotation, scale of 1 block wide.
        return RenderUtils.getMatrix(vec,rot, 1).apply(getScale());
    }

    /**
     * Override to provide your own scale.
     * @return scale
     */
    protected Scale getScale(){
        return scale;
    }

    /**
     * Override to provide your own animation.
     * @return the Animation object to use for model modification.
     */
    protected Animation getAnimation(){
        //Empty anim
        return new Animation(0);
    }

    /**
     * Override to provide your model.
     * @return the loaded CCModel
     */
    public abstract CCModel getModel();
}