package com.vicious.viciouscore.client.gui.widgets.glrendered;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class WidgetRectangularBorder extends VCWidget {
    protected int thickness;
    protected int color;
    protected float opacity;
    public WidgetRectangularBorder(RootWidget root, int x, int y, int w, int h, int color, float opacity, int thickness) {
        super(root,x,y,w,h);
        this.thickness=thickness;
        this.color = color;
        this.opacity = opacity;
    }


    @Override
    public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(255.0F, 255.0F, 255.0F, this.opacity);
        int x = actualPosition.x;
        int y = actualPosition.y;
        AbstractContainerScreen.fill(stack,x,y,x+thickness,y+height-thickness,color);
        AbstractContainerScreen.fill(stack,x,y+height-thickness,x+width-thickness,y+height,color);
        AbstractContainerScreen.fill(stack,x+width-thickness,y+thickness,x+width,y+height,color);
        AbstractContainerScreen.fill(stack,x+thickness,y,x+width,y+thickness,color);
    }

    public int getThickness(){
        return thickness;
    }
}
