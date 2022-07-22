package com.vicious.viciouscore.client.gui.widgets;


import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public abstract class WidgetDraggable extends WidgetInteraction{

    public WidgetDraggable(int x, int y, int w, int h, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, text, source);
    }
    public WidgetDraggable(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, tv, text, source);
    }
    public abstract void attemptDrag(int mouseX, int mouseY);
}
