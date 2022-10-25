package com.vicious.viciouscore.client;

import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.*;

public interface RenderTools {
    default void shade(Color color){
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(color.getRed(),color.getGreen(),color.getBlue(),1);
    }
    default void shade(Color color, float opacity){
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(color.getRed(),color.getGreen(),color.getBlue(),opacity);
    }
    default void stopShade(){
        RenderSystem.disableBlend();
    }
    default void stopScissor(){
        RenderSystem.disableScissor();
    }
}
