package com.vicious.viciouscore.client.render;

import codechicken.lib.render.CCModel;
import com.vicious.viciouscore.client.render.animation.Animation;
import net.minecraft.util.ResourceLocation;

public interface ICCModelConsumer {
    /**
     * Override to provide your models.
     * @return the loaded CCModel
     */
    CCModel getModel();

    /**
     * Override to provide the models animator
     * @return
     */
    default Animation getAnimation(){
        return Animation.empty();
    }
    default ResourceLocation getModelTexture(){
        return null;
    }
}
