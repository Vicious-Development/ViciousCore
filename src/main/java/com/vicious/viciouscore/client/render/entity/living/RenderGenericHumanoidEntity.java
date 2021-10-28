package com.vicious.viciouscore.client.render.entity.living;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.client.render.animation.Animation;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderGenericHumanoidEntity<T extends EntityLiving> extends RenderEntityLiving<T> {
    //Used to scale up the UV area when textures of larger factor are used.
    protected static int baseTextureFactor = 4;
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
        int uvFactor = baseTextureFactor/getTextureFactor();
        CCModel leg = getLegModel().copy().apply(new Translation(-0.5,1,0));
        CCModel body = getBodyModel().copy().apply(new Translation(0,3,0));
        CCModel arm = getArmModel().copy().apply(new Translation(-1.5,3,0));
        CCModel head = getHeadModel().copy().apply(new Translation(0,5,0));

        //Changes texture definition. By default, our models assume every texture is on a scale of 4x by 4y.
        if(uvFactor != 1){
            leg.shrinkUVs(uvFactor);
            body.shrinkUVs(uvFactor);
            arm.shrinkUVs(uvFactor);
            head.shrinkUVs(uvFactor);
        }
        CCRenderState rs = startAndBind(ResourceCache.TESTLIMBSPRITELOCATION);
        setLighting(ViciousRenderManager.getLightingBrightness(entity.getPosition()));
        //Render Legs
        getLegAnimation().runModelFrameAndRender(leg,x,y,z,yaw,partialticks,rs,mat);
        getLegAnimation().runModelFrameAndRender(leg.apply(new Translation(1,0,0)),x,y,z,yaw,partialticks,rs,mat);

        //Render arms
        getArmAnimation().runModelFrameAndRender(arm,x,y,z,yaw,partialticks,rs,mat);
        getArmAnimation().runModelFrameAndRender(arm.apply(new Translation(3,0,0)),x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

        //Render Body
        rs = startAndBind(ResourceCache.TESTBODYSPRITELOCATION);
        getBodyAnimation().runModelFrameAndRender(body,x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

        //Render Head
        rs = startAndBind(ResourceCache.TESTHEADSPRITELOCATION);
        getHeadAnimation().runModelFrameAndRender(head,x,y,z,yaw,partialticks,rs,mat);
        rs.draw();

    }

    /**
     * Override to change models.
     * @return
     */
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

    /**
     * Override to change texture definition level. Base level if 4px. To increase it to 16px, return 16.
     * When rendering, the UVs will be "shrunk" by a factor of 4/getTextureFactor() and will read pixels from a larger texture space.
     * @return
     */
    public int getTextureFactor(){
        return baseTextureFactor;
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

    /**
     * Override these to provide your own textures. Make sure to override texture factor as well.
     * All your textures should be relatively the same, Assuming f is the texture factor, your textures should have the following dimensions.
     * Where the values are the lengths of the LONGEST parts of the texture.
     * the head should be 2f*2f, limbs f*2f, body, 2f*2f.
     * @return
     */
    public ResourceLocation getHeadTexture(){
        return ResourceCache.TESTHEADSPRITELOCATION;
    }
    public ResourceLocation getLegTexture(){
        return ResourceCache.TESTLIMBSPRITELOCATION;
    }
    public ResourceLocation getArmTexture(){
        return ResourceCache.TESTLIMBSPRITELOCATION;
    }
    public ResourceLocation getBodyTexture(){
        return ResourceCache.TESTBODYSPRITELOCATION;
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
