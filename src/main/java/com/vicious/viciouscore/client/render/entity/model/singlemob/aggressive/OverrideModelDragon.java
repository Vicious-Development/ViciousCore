package com.vicious.viciouscore.client.render.entity.model.singlemob.aggressive;

import com.vicious.viciouscore.client.render.entity.model.IOverrideModel;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OverrideModelDragon extends ModelDragon implements IOverrideModel {
    public Queue<Runnable> transforms = new LinkedList<>();
    private Map<String, Field> partMap = new HashMap<>();

    public OverrideModelDragon(ModelDragon og) {
        super(1F);
        clone(og,partMap);
    }

    public void applicate(EntityModelOverride<?> configurations) {
        applicate(partMap, configurations);
    }

    /**
     * Just a side note, making this shit work took fucking forever.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        float partialTicks = (float) Reflection.accessField(this,"partialTicks");
        this.overrideRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        EntityDragon entitydragon = (EntityDragon)entityIn;
        float f = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * partialTicks;
        ((ModelRenderer)Reflection.accessField(this,"jaw")).rotateAngleX = (float)(Math.sin((f * ((float)Math.PI * 2F))) + 1.0D) * 0.2F;
        float f1 = (float)(Math.sin((f * ((float)Math.PI * 2F) - 1.0F)) + 1.0D);
        f1 = (f1 * f1 + f1 * 2.0F) * 0.05F;
        GlStateManager.translate(0.0F, f1 - 2.0F, -3.0F);
        GlStateManager.rotate(f1 * 2.0F, 1.0F, 0.0F, 0.0F);
        float f2 = -30.0F;
        float f4 = 0.0F;
        float f5 = 1.5F;
        double[] adouble = entitydragon.getMovementOffsets(6, partialTicks);
        Class<?>[] urc = new Class[]{Double.TYPE};
        Method updateRotations = Reflection.getMethod(this,"updateRotations", urc);
        float f6 = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{entitydragon.getMovementOffsets(5, partialTicks)[0] - entitydragon.getMovementOffsets(10, partialTicks)[0]});
        float f7 = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{entitydragon.getMovementOffsets(5, partialTicks)[0] + (double)(f6 / 2.0F)});
        float f8 = f * ((float)Math.PI * 2F);
        f2 = 20.0F;
        float f3 = -12.0F;
        ModelRenderer spine = (ModelRenderer) Reflection.accessField(this,"spine");
        ModelRenderer head = (ModelRenderer) Reflection.accessField(this,"head");
        ModelRenderer body = (ModelRenderer) Reflection.accessField(this,"body");
        ModelRenderer wing = (ModelRenderer) Reflection.accessField(this,"wing");
        for (int i = 0; i < 5; ++i)
        {
            double[] adouble1 = entitydragon.getMovementOffsets(5 - i, partialTicks);
            float f9 = (float)Math.cos((double)((float)i * 0.45F + f8)) * 0.15F;
            spine.rotateAngleY = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{adouble1[0] - adouble[0]}) * 0.017453292F * 1.5F;
            spine.rotateAngleX = f9 + entitydragon.getHeadPartYOffset(i, adouble, adouble1) * 0.017453292F * 1.5F * 5.0F;
            spine.rotateAngleZ = -(float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{adouble1[0] - (double)f7}) * 0.017453292F * 1.5F;
            spine.rotationPointY = f2;
            spine.rotationPointZ = f3;
            spine.rotationPointX = f4;
            f2 = (float)((double)f2 + Math.sin(spine.rotateAngleX) * 10.0D);
            f3 = (float)((double)f3 - Math.cos(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
            f4 = (float)((double)f4 - Math.sin(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
            spine.render(scale);
        }

        head.rotationPointY = f2;
        head.rotationPointZ = f3;
        head.rotationPointX = f4;
        double[] adouble2 = entitydragon.getMovementOffsets(0, partialTicks);

        head.rotateAngleY = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{(adouble2[0] - adouble[0])}) * 0.017453292F;
        head.rotateAngleX = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{((double)entitydragon.getHeadPartYOffset(6, adouble, adouble2))}) * 0.017453292F * 1.5F * 5.0F;
        head.rotateAngleZ = -(float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{(adouble2[0] - (double)f7)}) * 0.017453292F;
        head.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-f6 * 1.5F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.0F, -1.0F, 0.0F);
        body.rotateAngleZ = 0.0F;
        body.render(scale);

        for (int j = 0; j < 2; ++j)
        {
            GlStateManager.enableCull();
            float f11 = f * ((float)Math.PI * 2F);
            wing.rotateAngleX = 0.125F - (float)Math.cos(f11) * 0.2F;
            wing.rotateAngleY = 0.25F;
            wing.rotateAngleZ = (float)(Math.sin(f11) + 0.125D) * 0.8F;
            ((ModelRenderer) Reflection.accessField(this,"wingTip")).rotateAngleZ = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
            ((ModelRenderer) Reflection.accessField(this,"rearLeg")).rotateAngleX = 1.0F + f1 * 0.1F;
            ((ModelRenderer) Reflection.accessField(this,"rearLegTip")).rotateAngleX = 0.5F + f1 * 0.1F;
            ((ModelRenderer) Reflection.accessField(this,"rearFoot")).rotateAngleX = 0.75F + f1 * 0.1F;
            ((ModelRenderer) Reflection.accessField(this,"frontLeg")).rotateAngleX = 1.3F + f1 * 0.1F;
            ((ModelRenderer) Reflection.accessField(this,"frontLegTip")).rotateAngleX = -0.5F - f1 * 0.1F;
            ((ModelRenderer) Reflection.accessField(this,"frontFoot")).rotateAngleX = 0.75F + f1 * 0.1F;
            wing.render(scale);
            ((ModelRenderer) Reflection.accessField(this,"frontLeg")).render(scale);
            ((ModelRenderer) Reflection.accessField(this,"rearLeg")).render(scale);
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);

            if (j == 0)
            {
                GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.disableCull();
        float f10 = -((float)Math.sin((f * ((float)Math.PI * 2F)))) * 0.0F;
        f8 = f * ((float)Math.PI * 2F);
        f2 = 10.0F;
        f3 = 60.0F;
        f4 = 0.0F;
        adouble = entitydragon.getMovementOffsets(11, partialTicks);

        for (int k = 0; k < 12; ++k)
        {
            adouble2 = entitydragon.getMovementOffsets(12 + k, partialTicks);
            f10 = (float)((double)f10 + Math.sin((double)((float)k * 0.45F + f8)) * 0.05000000074505806D);
            spine.rotateAngleY = ((float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{adouble2[0] - adouble[0]}) * 1.5F + 180.0F) * 0.017453292F;
            spine.rotateAngleX = f10 + (float)(adouble2[1] - adouble[1]) * 0.017453292F * 1.5F * 5.0F;
            spine.rotateAngleZ = (float) Reflection.invokeMethod(this,updateRotations,urc, new Object[]{adouble2[0] - (double)f7}) * 0.017453292F * 1.5F;
            spine.rotationPointY = f2;
            spine.rotationPointZ = f3;
            spine.rotationPointX = f4;
            f2 = (float)((double)f2 + Math.sin((double)spine.rotateAngleX) * 10.0D);
            f3 = (float)((double)f3 - Math.cos((double)spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
            f4 = (float)((double)f4 - Math.sin((double)spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
            spine.render(scale);
        }

        GlStateManager.popMatrix();
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
