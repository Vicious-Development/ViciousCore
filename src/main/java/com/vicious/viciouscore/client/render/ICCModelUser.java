package com.vicious.viciouscore.client.render;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public interface ICCModelUser {
    //A scale of 1 meter.
    Scale DEFAULT_SCALE = new Scale(0.5,0.5,0.5);

    /**
     * Starts drawing and binds a texture.
     * @param texture
     * @return The render state to call #draw on.
     */
    default CCRenderState startAndBind(ResourceLocation texture){
        CCRenderState rs = CCRenderState.instance();
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        ViciousRenderManager.bindTexture(texture);
        return rs;
    }

    /**
     * Applies GL rotations depending on a facing value.
     * @param facing
     */
    default void rotateGL(EnumFacing facing){
        //Minecraft has horizontal but not vertical angle? Excuse me why?
        if(facing.getHorizontalIndex() == -1) GlStateManager.rotate(90,facing.getDirectionVec().getX(),facing.getDirectionVec().getY(),facing.getDirectionVec().getZ());
        else GlStateManager.rotate(facing.getHorizontalAngle(),facing.getDirectionVec().getX(),facing.getDirectionVec().getY(),facing.getDirectionVec().getZ());
    }

    /**
     * Draws the GL.
     * @param rs
     */
    default void draw(CCRenderState rs){
        rs.draw();
    }

    /**
     * Override to provide your own scale.
     * @return scale
     */
    default Scale getScale(){
        return DEFAULT_SCALE;
    }

    /**
     * Override to provide your own matrix.
     * @param x Camera x dist
     * @param y Camera y dist
     * @param z Camera z dist
     * @return Matrix with no rotation and of supplied scale
     */
    default Matrix4 getMatrix(double x, double y, double z){
        //Create the default rendering matrix (no rotation, scale of 1 block wide.
        return RenderUtils.getMatrix(new Vector3(x,y,z), getRotation(), 1).apply(getScale());
    }
    default Matrix4 getMatrix(Vector3 vec, Rotation rot){
        //Create the default rendering matrix (no rotation, scale of 1 block wide.
        return RenderUtils.getMatrix(vec,rot, 1).apply(getScale());
    }
    default Matrix4 getMatrix(){
        return RenderUtils.getMatrix(getVector(), getRotation(), 1).apply(getScale());
    }
    default Vector3 getVector(){
        return new Vector3(0,0,0);
    }
    default Rotation getRotation(){
        return new Rotation(0,0,0,1);
    }
    /**
     * Override to provide your own animation.
     * @return the Animation object to use for models modification.
     */
    default void setLighting(float brightness){
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness, brightness);
    }
}
