package com.vicious.viciouscore.client.render.entity.model.singlemob.passive;

import com.vicious.viciouscore.client.configuration.EntityModelOverrideCFG;
import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelBat extends ModelBat implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelBat(ModelBat og) {
        clone(og,partMap);
    }

    public void applicate(EntityModelOverrideCFG<?> configurations) {
        applicate(partMap, configurations);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        ((ModelRenderer)Reflection.accessField(this,"batHead")).render(scale);
        ((ModelRenderer)Reflection.accessField(this,"batBody")).render(scale);
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
