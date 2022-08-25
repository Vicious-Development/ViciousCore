package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.common.util.server.BetterChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class WidgetText extends VCWidget {
    private Component component;
    public WidgetText(RootWidget root, int x, int y, int w, int h, Component text) {
        super(root, x, y, w, h);
        this.component=text;
    }
    public WidgetText(RootWidget root, int x, int y, int w, int h, Object... betterchatmessage) {
        super(root, x, y, w, h);
        this.component = BetterChatMessage.from(betterchatmessage).component;
    }

    @Override
    protected void doGLTransformations(PoseStack stack) {
        super.doGLTransformations(stack);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        int oldWidth = width;
        int oldHeight = height;
        width = Minecraft.getInstance().font.draw(stack, component, actualPosition.x, actualPosition.y, Color.WHITE.getRGB());
        height = Minecraft.getInstance().font.lineHeight;
        if(oldWidth != width || oldHeight != height){
            calculateVectors();
        }
    }

    public void setText(Object... betterchatmessage) {
        this.component = BetterChatMessage.from(betterchatmessage).component;
    }
}
