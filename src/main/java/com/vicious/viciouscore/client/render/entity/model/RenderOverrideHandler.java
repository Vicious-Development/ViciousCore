package com.vicious.viciouscore.client.render.entity.model;

import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;

@SuppressWarnings("unchecked")
public class RenderOverrideHandler {
    /**
     * Used to allow custom mob animations. Works on anything with a mainModel of ModelBiped.
     * In vanilla this includes Skeletons, Strays, Zombies, Zombie Villagers, Zombie Pigmen, etc.
     * We need to do this because vanilla by default applies rotations on doRender() that need to be overriden.
     * @param entityRenderer
     * @return
     */
    public static OverrideModelBiped overrideModelBiped(RenderBiped<?> entityRenderer) {
        //Override the entity model so we can apply changes.
        if (!(entityRenderer.getMainModel() instanceof OverrideModelBiped)) {
            ModelBiped model = (ModelBiped) entityRenderer.getMainModel();
            Reflection.setField(entityRenderer, new OverrideModelBiped(model), "mainModel");
        }
        return (OverrideModelBiped) entityRenderer.getMainModel();
    }
}
