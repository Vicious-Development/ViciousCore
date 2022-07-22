package com.vicious.viciouscore.client.gui.widgets;

import com.drathonix.zeroenergy.client.util.Vector2i;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;

public class WidgetInteraction extends WidgetImage{
    public WidgetInteraction(int x, int y, int w, int h, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, text, source);
    }
    public WidgetInteraction(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, tv, text, source);
    }
    public WidgetInteraction getUpdatedWidget(){
        WidgetInteraction updated = new WidgetInteraction(x, y, width, height, startPos, getMessage(), source);
        updated.children = updateChildren();
        return updated;
    }
}
