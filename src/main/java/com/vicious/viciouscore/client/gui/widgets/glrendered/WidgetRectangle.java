package com.vicious.viciouscore.client.gui.widgets.glrendered;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.IAlphaRGB;
import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Todo: relocate to vicious core and generify for other projects.
 */
public class WidgetRectangle extends VCWidget implements IAlphaRGB {
    protected int color;
    protected float opacity;
    public WidgetRectangle(RootWidget root, int x, int y, int w, int h, int color, float opacity) {
        super(root, x, y, w, h);
        this.color=color;
        this.opacity=opacity;
    }

    @Override
    public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        Vector2i topleft = getExtents().TOPLEFT;
        Vector2i bottomright = getExtents().BOTTOMRIGHT;
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(255,255,255,opacity);
        AbstractContainerScreen.fill(stack,topleft.x,topleft.y,bottomright.x, bottomright.y, color);
    }

    @Override
    public int getRGB() {
        return color;
    }

    @Override
    public void setRGB(int rgb) {
        color=rgb;
    }

    @Override
    public float getOpacity() {
        return opacity;
    }

    @Override
    public void setOpacity(float opacity) {
        this.opacity=opacity;
    }
}
