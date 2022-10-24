package com.vicious.viciouscore.client.gui.widgets.glrendered;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import java.awt.*;

/**
 * Todo: relocate to vicious core and generify for other projects.
 */
public class WidgetRectangle<T extends WidgetRectangle<T>> extends VCWidget<T> {
    public WidgetRectangle(RootWidget root, int x, int y, int w, int h) {
        super(root, x, y, w, h);
    }

    @Override
    public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        Vector2i topleft = getExtents().TOPLEFT;
        Vector2i bottomright = getExtents().BOTTOMRIGHT;
        //RenderSystem.enableBlend();
        //RenderSystem.setShaderColor(255,255,255,opacity);
        AbstractContainerScreen.fill(stack,topleft.x,topleft.y,bottomright.x, bottomright.y, Color.WHITE.getRGB());
    }
}
