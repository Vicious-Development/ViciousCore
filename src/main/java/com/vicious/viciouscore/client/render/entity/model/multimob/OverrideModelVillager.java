package com.vicious.viciouscore.client.render.entity.model.multimob;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.ModelRendererConfiguration;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelVillager extends ModelVillager implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelVillager(ModelVillager og) {
        super(1f);
        clone(og,partMap);
    }

    public void applicate(EntityModelOverride<?> configurations) {
        applicate(partMap, configurations);
    }
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.villagerHead.render(scale);
        this.villagerBody.render(scale);
        this.rightVillagerLeg.render(scale);
        this.leftVillagerLeg.render(scale);
        this.villagerArms.render(scale);
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

