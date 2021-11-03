package com.vicious.viciouscore.client.render.entity.model.singlemob.passive;

import com.vicious.viciouscore.client.configuration.EntityModelOverrideCFG;
import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelHorse extends ModelHorse implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelHorse(ModelHorse og) {
        clone(og,partMap);
    }

    public void applicate(EntityModelOverrideCFG<?> configurations) {
        applicate(partMap, configurations);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        AbstractHorse abstracthorse = (AbstractHorse)entityIn;
        float f = abstracthorse.getGrassEatingAmount(0.0F);
        boolean flag = abstracthorse.isChild();
        boolean flag1 = !flag && abstracthorse.isHorseSaddled();
        boolean flag2 = abstracthorse instanceof AbstractChestHorse;
        boolean flag3 = !flag && flag2 && ((AbstractChestHorse)abstracthorse).hasChest();
        float f1 = abstracthorse.getHorseSize();
        boolean flag4 = abstracthorse.isBeingRidden();

        if (flag1)
        {
            ((ModelRenderer) Reflection.accessField(this,"horseFaceRopes")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseSaddleBottom")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseSaddleFront")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseSaddleBack")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseLeftSaddleRope")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseLeftSaddleMetal")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseRightSaddleRope")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseRightSaddleMetal")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseLeftFaceMetal")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseRightFaceMetal")).render(scale);
            if (flag4)
            {
                ((ModelRenderer) Reflection.accessField(this,"horseLeftRein")).render(scale);
                ((ModelRenderer) Reflection.accessField(this,"horseRightRein")).render(scale);
            }
        }

        if (flag)
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(f1, 0.5F + f1 * 0.5F, f1);
            GlStateManager.translate(0.0F, 0.95F * (1.0F - f1), 0.0F);
        }
        ((ModelRenderer) Reflection.accessField(this,"backLeftLeg")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"backLeftShin")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"backLeftHoof")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"backRightLeg")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"backRightHoof")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"frontLeftLeg")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"frontLeftShin")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"frontRightLeg")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"frontRightShin")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"frontRightHoof")).render(scale);

        if (flag)
        {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(f1, f1, f1);
            GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
        }
        ((ModelRenderer) Reflection.accessField(this,"body")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"tailBase")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"tailMiddle")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"tailTip")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"neck")).render(scale);
        ((ModelRenderer) Reflection.accessField(this,"mane")).render(scale);

        if (flag)
        {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            float f2 = 0.5F + f1 * f1 * 0.5F;
            GlStateManager.scale(f2, f2, f2);

            if (f <= 0.0F)
            {
                GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
            }
            else
            {
                GlStateManager.translate(0.0F, 0.9F * (1.0F - f1) * f + 1.35F * (1.0F - f1) * (1.0F - f), 0.15F * (1.0F - f1) * f);
            }
        }

        if (flag2)
        {
            ((ModelRenderer) Reflection.accessField(this,"muleLeftEar")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"muleRightEar")).render(scale);
        }
        else
        {
            ((ModelRenderer) Reflection.accessField(this,"horseLeftEar")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"horseRightEar")).render(scale);
        }
        ((ModelRenderer) Reflection.accessField(this,"head")).render(scale);

        if (flag)
        {
            GlStateManager.popMatrix();
        }

        if (flag3)
        {
            ((ModelRenderer) Reflection.accessField(this,"muleLeftChest")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"muleRightChest")).render(scale);
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
