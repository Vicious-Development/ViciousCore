package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.Vector2i;
import com.vicious.viciouscore.common.util.server.BetterChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class WidgetText<T extends WidgetText<T>> extends VCWidget<T> {
    private Component component;
    public WidgetText(RootWidget root, int x, int y, int w, int h, Component text) {
        super(root, x, y, w, h);
        this.component=text;
        addGL(RenderStage.SELFPRE,(stack -> {
            RenderSystem.disableBlend();
        }));
    }
    public WidgetText(RootWidget root, int x, int y, int w, int h, Object... betterchatmessage) {
        super(root, x, y, w, h);
        this.component = BetterChatMessage.from(betterchatmessage).component;
    }

    @Override
    protected void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
        int oldWidth = getWidth();
        int oldHeight = getHeight();
        int width = Minecraft.getInstance().font.draw(stack, component, actualPosition.x, actualPosition.y, Color.WHITE.getRGB());
        int height = Minecraft.getInstance().font.lineHeight;
        if(oldWidth != width || oldHeight != height){
            dimensions(new Vector2i(width,height));
        }
    }
    public void setText(Object... betterchatmessage) {
        this.component = BetterChatMessage.from(betterchatmessage).component;
    }
}
