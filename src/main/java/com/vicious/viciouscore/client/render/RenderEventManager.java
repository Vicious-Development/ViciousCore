package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.client.render.model.OverrideModelBiped;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
public class RenderEventManager {
    static float angle = 0;
    @SubscribeEvent
    public static void onHumanoidHeld(RenderLivingEvent.Pre<?> ev){
        EntityLivingBase entity = ev.getEntity();
        Render<?> render = ev.getRenderer();
        //Entity is compatible with our custom models.
        if(render instanceof RenderBiped){
            //TEMP
            RenderLiving<?> entityRenderer = (RenderLiving<?>) ev.getRenderer();
            if (!(entityRenderer.getMainModel() instanceof OverrideModelBiped)) {
                ModelBiped model = (ModelBiped) entityRenderer.getMainModel();
                Reflection.setField(entityRenderer, new OverrideModelBiped(model), "mainModel");
            }
            if(entityRenderer.getMainModel() instanceof OverrideModelBiped){
                OverrideModelBiped model = (OverrideModelBiped) entityRenderer.getMainModel();
                //Override Left arm position.
                model.transforms.offer(()->{
                    model.bipedLeftArm.rotateAngleY+=angle;
                    model.bipedLeftArm.rotateAngleZ+=angle;
                    model.bipedRightArm.rotateAngleY+=angle;
                    model.bipedRightArm.rotateAngleX+=angle;
                    model.bipedBody.rotateAngleZ+=angle;
                    model.bipedBody.rotateAngleX+=angle;
                    model.bipedLeftLeg.rotateAngleZ+=angle;
                    model.bipedLeftLeg.rotateAngleX+=angle;
                    model.bipedRightLeg.rotateAngleZ+=angle;
                    model.bipedRightLeg.rotateAngleY+=angle;
                    model.bipedHead.rotateAngleZ+=angle;
                    model.bipedHead.rotateAngleY+=angle;

                });
            }
            //TEMP
            ItemStack itemHeld = entity.getHeldItemMainhand();
            //Is holding a render overrider, cancel that bitch.
            if(itemHeld.getItem() instanceof IRenderOverride) {
                ((IRenderOverride)itemHeld.getItem()).renderEntity(ev);
            }
            angle+=0.05;
        }
    }
}
