package com.vicious.viciouscore.client.render.entity.model.singlemob.passive;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelOcelot extends ModelOcelot implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelOcelot(ModelOcelot og) {
        clone(og,partMap);
    }

    public void applicate(EntityModelOverride<?> configurations) {
        applicate(partMap, configurations);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 10.0F * scale, 4.0F * scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotHead")).render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBody")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBackLeftLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBackRightLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotFrontLeftLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotTail")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotTail2")).render(scale);
            GlStateManager.popMatrix();
        }
        else
        {
            ((ModelRenderer)Reflection.accessField(this,"ocelotHead")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBody")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBackLeftLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotBackRightLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotFrontLeftLeg")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotTail")).render(scale);
            ((ModelRenderer)Reflection.accessField(this,"ocelotTail2")).render(scale);
        }
        resetTransformations(boxList);
    }

    public void overrideRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        //Runs rotations after MC has done its own bullshit.
        while(!transforms.isEmpty()){
            transforms.remove().run();
        }
    }

    @Override
    public void queueTransformer(Runnable in) {
        transforms.offer(in);
    }

    @Override
    public void ignoreHandSide(EnumHandSide in) {
        //donothing
    }
}
