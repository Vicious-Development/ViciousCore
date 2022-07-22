package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WidgetNoInteraction extends WidgetImage{
    public WidgetNoInteraction(int x, int y, int w, int h, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, text, source);
    }
    public WidgetNoInteraction(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, tv, text, source);
    }
    public WidgetNoInteraction getUpdatedWidget(){
        WidgetNoInteraction updated = new WidgetNoInteraction(x, y, width, height, startPos, getMessage(), source);
        updated.children = updateChildren();
        return updated;
    }

    //A widget with no user interaction, just visual.
    @Override
    public boolean isHovered() {
        return false;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
}
