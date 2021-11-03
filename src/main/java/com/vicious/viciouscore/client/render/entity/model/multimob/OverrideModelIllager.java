package com.vicious.viciouscore.client.render.entity.model.multimob;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.configuration.EntityModelOverrideCFG;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelIllager extends ModelIllager implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelIllager(ModelIllager og) {
        super(1F,0F,og.textureWidth, og.textureHeight);
        clone(og,partMap);
    }


    public void applicate(EntityModelOverrideCFG<?> configurations) {
        applicate(partMap, configurations);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.head.render(scale);
        this.body.render(scale);
        this.leg0.render(scale);
        this.leg1.render(scale);
        AbstractIllager abstractillager = (AbstractIllager)entityIn;

        if (abstractillager.getArmPose() == AbstractIllager.IllagerArmPose.CROSSED)
        {
            this.arms.render(scale);
        }
        else
        {
            this.rightArm.render(scale);
            this.leftArm.render(scale);
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
