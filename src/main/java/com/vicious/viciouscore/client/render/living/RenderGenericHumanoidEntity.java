package com.vicious.viciouscore.client.render.living;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.*;
import com.vicious.viciouscore.client.render.GenericRenderableEntity;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.common.util.ResourceCache;
import com.vicious.viciouscore.common.util.ViciousLoader;
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
    public RenderGenericHumanoidEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float partialticks) {
        //Get the current render state.
        CCRenderState rs = CCRenderState.instance();

        //Set the lighting values to the local light brightness (0-200).
        setLighting(ViciousRenderManager.getLightingBrightness(entity.getPosition()));

        //Bind the texture that our model UV Maps to.
        ViciousRenderManager.bindTexture(ResourceCache.TESTLIMBSPRITELOCATION);

        //Start the render
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);

        //Get the matrix data for the camera distance.
        Matrix4 mat = getMatrix(x,y,z);
        CCModel translatedLeg = getLegModel().copy().apply(new Translation(0,1,0)).apply(new Rotation(yaw,0,1,0));

        //Apply animation modifications.
        getAnimation().runModelFrameAndRender(translatedLeg,x,y,z,yaw,partialticks,rs,mat);
        getAnimation().runModelFrameAndRender(translatedLeg.apply(new Translation(1,0,0)),x,y,z,yaw,partialticks,rs,mat);

        //Do this otherwise the client will crash. Crashing is bad.
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
