package com.vicious.viciouscore.client.render.living;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.*;
import com.vicious.viciouscore.client.render.GenericRenderableEntity;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.client.render.animation.Animation;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderGenericHumanoidEntity<T extends EntityLiving> extends GenericRenderableEntityLiving<T> {
    protected static CCModel defaultHeadModel = ViciousLoader.loadViciousModel("entity/obj/head.obj").backfacedCopy();
    protected static CCModel defaultLimbModel = ViciousLoader.loadViciousModel("entity/obj/limb.obj").backfacedCopy();
    protected static CCModel defaultBodyModel = ViciousLoader.loadViciousModel("entity/obj/body.obj").backfacedCopy();

    public RenderGenericHumanoidEntity(RenderManager in){
        super(in);
    }
    public RenderGenericHumanoidEntity(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float partialticks) {
        //Get the matrix data for the camera distance.
        Matrix4 mat = getMatrix(new Vector3(x,y,z),new Rotation(-entity.rotationYawHead/(180F / (float)Math.PI),0,1,0));
        CCModel leg = getLegModel().copy().apply(new Translation(-0.5,1,0));
        CCModel body = getBodyModel().copy().apply(new Translation(0,3,0));
        CCModel arm = getArmModel().copy().apply(new Translation(-1.5,3,0));
        CCModel head = getHeadModel().copy().apply(new Scale(2,2,2)).apply(new Translation(0,5,0));



        CCRenderState rs = CCRenderState.instance();
        setLighting(ViciousRenderManager.getLightingBrightness(entity.getPosition()));
        ViciousRenderManager.bindTexture(ResourceCache.TESTLIMBSPRITELOCATION);
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        //Render Legs
        getLegAnimation().runModelFrameAndRender(leg,x,y,z,yaw,partialticks,rs,mat);
        getLegAnimation().runModelFrameAndRender(leg.apply(new Translation(1,0,0)),x,y,z,yaw,partialticks,rs,mat);

        //Render arms
        getArmAnimation().runModelFrameAndRender(arm,x,y,z,yaw,partialticks,rs,mat);
        getArmAnimation().runModelFrameAndRender(arm.apply(new Translation(3,0,0)),x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

        //Render Body
        ViciousRenderManager.bindTexture(ResourceCache.TESTBODYSPRITELOCATION);
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        getBodyAnimation().runModelFrameAndRender(body,x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

        //Render Head
        ViciousRenderManager.bindTexture(ResourceCache.TESTHEADSPRITELOCATION);
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        getHeadAnimation().runModelFrameAndRender(head,x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

    }
    public CCModel getHeadModel(){
        return defaultHeadModel;
    }
    public CCModel getBodyModel(){
        return defaultBodyModel;
    }
    public CCModel getArmModel(){
        return defaultLimbModel;
    }
    public CCModel getLegModel(){
        return defaultLimbModel;
    }

    public Animation getHeadAnimation(){
        return Animation.empty();
    }
    public Animation getArmAnimation(){
        return Animation.empty();
    }
    public Animation getLegAnimation(){
        return Animation.empty();
    }
    public Animation getBodyAnimation(){
        return Animation.empty();
    }

    @Override
    public CCModel getModel() {
        return defaultBodyModel;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
