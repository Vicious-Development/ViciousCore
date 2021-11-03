package com.vicious.viciouscore.client.render.entity.model.multimob;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.ModelRendererConfiguration;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.*;

//Unused due to no RenderQuadruped actually existing. must override children.
/*
public class OverrideModelQuadruped extends ModelQuadruped implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    public List<EnumHandSide> ignoreHandSides = new ArrayList<>();
    private Map<String, Field> partMap = new HashMap<>();

    private boolean doRemove = false;
    public OverrideModelQuadruped(ModelQuadruped og) {
        super(1,1);
        clone(og,partMap);
    }

    public void overrideRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        //Runs rotations after MC has done its own bullshit.
        while(!transforms.isEmpty()){
            transforms.remove().run();
        }
    }



    public void applicate(EntityModelOverride<?> configurations) {
        applicate(partMap, configurations);
    }

    @Override
    public void queueTransformer(Runnable in) {
        transforms.offer(in);
    }

    @Override
    public void ignoreHandSide(EnumHandSide in) {
        ignoreHandSides.add(in);
    }


    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            GlStateManager.popMatrix();
        }
        else
        {
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
        GlStateManager.popMatrix();
        resetTransformations(this.boxList);
    }
}*/
