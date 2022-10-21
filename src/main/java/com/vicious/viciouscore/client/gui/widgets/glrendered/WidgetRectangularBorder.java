package com.vicious.viciouscore.client.gui.widgets.glrendered;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

public class WidgetRectangularBorder<T extends WidgetRectangularBorder<T>> extends VCWidget<T> {
    protected int thickness;
    public WidgetRectangularBorder(RootWidget root, int x, int y, int w, int h, int thickness) {
        super(root,x,y,w,h);
        this.thickness=thickness;
    }


    @Override
    public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        //RenderSystem.enableBlend();
       // RenderSystem.setShaderColor(255.0F, 255.0F, 255.0F, this.opacity);
        int x = actualPosition.x;
        int y = actualPosition.y;
        int height = getHeight();
        int width = getWidth();
        int rgb = Color.WHITE.getRGB();
        AbstractContainerScreen.fill(stack,x,y,x+thickness,y+height-thickness, rgb);
        AbstractContainerScreen.fill(stack,x,y+height-thickness,x+width-thickness,y+height,rgb);
        AbstractContainerScreen.fill(stack,x+width-thickness,y+thickness,x+width,y+height,rgb);
        AbstractContainerScreen.fill(stack,x+thickness,y,x+width,y+thickness,rgb);
    }

    public int getThickness(){
        return thickness;
    }
}
