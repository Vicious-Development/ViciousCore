package com.vicious.viciouscore.client.render.entity.model;

import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.ModelRendererConfiguration;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Overridden version of ModelBiped which changes how the game modifies the model on client rendering.
 * This model type gets injected into the mainModel field when RenderOverrideHandler#overrideBipedModel is called.
 */
public class OverrideModelBiped extends ModelBiped {
    public Queue<Runnable> transforms = new LinkedList<>();
    public List<EnumHandSide> ignoreHandSides = new ArrayList<>();
    private boolean doRemove = false;

    public OverrideModelBiped()
    {
        this(0.0F);
    }

    public OverrideModelBiped(float modelSize)
    {
        this(modelSize, 0.0F, 64, 32);
    }

    public OverrideModelBiped(float modelSize, float yOffset, int textureWidthIn, int textureHeightIn)
    {
        super(modelSize,yOffset,textureWidthIn,textureHeightIn);
    }
    public OverrideModelBiped(ModelBiped og) {
        this.leftArmPose = og.leftArmPose;
        this.rightArmPose = og.rightArmPose;
        this.textureWidth = og.textureWidth;
        this.textureHeight = og.textureHeight;
        this.bipedHead = add(og.bipedHead);
        this.bipedHeadwear = add(og.bipedHeadwear);
        this.bipedBody = add(og.bipedBody);
        this.bipedRightArm = add(og.bipedRightArm);
        this.bipedLeftArm = add(og.bipedLeftArm);
        this.bipedRightLeg = add(og.bipedRightLeg);
        this.bipedLeftLeg = add(og.bipedLeftLeg);
    }

    public void applicate(EntityModelOverride<ModelBiped> configurations) {
        ModelRendererConfiguration leftarmcfg = configurations.getPartConfiguration("bipedLeftArm");
        ModelRendererConfiguration rightarmcfg = configurations.getPartConfiguration("bipedRightArm");
        ModelRendererConfiguration leftlegcfg = configurations.getPartConfiguration("bipedLeftLeg");
        ModelRendererConfiguration rightlegcfg = configurations.getPartConfiguration("bipedRightLeg");
        ModelRendererConfiguration bodycfg = configurations.getPartConfiguration("bipedBody");
        ModelRendererConfiguration headcfg = configurations.getPartConfiguration("bipedHead");
        ModelRendererConfiguration headwearcfg = configurations.getPartConfiguration("bipedHeadwear");
        if(leftarmcfg.active.getBoolean()) applicatePart(bipedLeftArm,leftarmcfg);
        if(rightarmcfg.active.getBoolean()) applicatePart(bipedRightArm,rightarmcfg);
        if(leftlegcfg.active.getBoolean()) applicatePart(bipedLeftLeg,leftlegcfg);
        if(rightlegcfg.active.getBoolean()) applicatePart(bipedRightLeg,rightlegcfg);
        if(bodycfg.active.getBoolean()) applicatePart(bipedBody,bodycfg);
        if(headcfg.active.getBoolean()) applicatePart(bipedHead,headcfg);
        if(headwearcfg.active.getBoolean()) applicatePart(bipedHeadwear,headwearcfg);
    }
    private void applicatePart(ModelRenderer part, ModelRendererConfiguration config){
        if(config.overrideRotation.getBoolean()){
            part.rotateAngleX=config.rx.value();
            part.rotateAngleY=config.ry.value();
            part.rotateAngleZ=config.rz.value();
        }
        if(config.overrideTranslation.getBoolean()){
            part.offsetX=config.tx.value();
            part.offsetY=config.ty.value();
            part.offsetZ=config.tz.value();
        }
    }
    private ModelRenderer add(ModelRenderer box) {
        boxList.add(box);
        return box;
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }

        GlStateManager.popMatrix();
    }

    public void overrideRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        //Runs rotations after MC has done its own bullshit.
        while(!transforms.isEmpty()){
            transforms.remove().run();
        }
    }

    /**
     * Handles arm rendering. If you don't want the item to move with the arm, you can add the hand side to ignoreHandSides.
     */
    @Override
    public void postRenderArm(float scale, EnumHandSide side) {
        if(ignoreHandSides.contains(side)){
            if(doRemove) ignoreHandSides.remove(side);
            doRemove = !doRemove;
            return;
        }
        super.postRenderArm(scale, side);
    }
}
