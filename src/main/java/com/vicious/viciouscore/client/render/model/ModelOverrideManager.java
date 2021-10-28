package com.vicious.viciouscore.client.render.model;

import com.vicious.viciouscore.client.render.model.OverrideModelBiped;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;

public class ModelOverrideManager {
    /**
     * Used to allow custom mob animations. Works on anything with a mainModel of ModelBiped.
     * In vanilla this includes Skeletons, Strays, Zombies, Zombie Villagers, Zombie Pigmen, etc.
     * We need to do this because vanilla by default applies rotations on doRender() that need to be overriden.
     * @param entityRenderer
     * @return
     */
    public static OverrideModelBiped overrideBiped(RenderLiving<?> entityRenderer) {
        if (!(entityRenderer.getMainModel() instanceof OverrideModelBiped)) {
            ModelBiped model = (ModelBiped) entityRenderer.getMainModel();
            Reflection.setField(entityRenderer, new OverrideModelBiped(model), "mainModel");
        }
        return (OverrideModelBiped) entityRenderer.getMainModel();
    }
}
