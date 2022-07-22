package com.vicious.viciouscore.client.gui.widgets;


import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

/*This class is different from the draggable class in that it actually
handles the dragging of the widget. This widget allows the object to be
moved freely without any interruption.
* */
public class WidgetFreeDrag extends WidgetDraggable{
    private boolean dragStarted;
    public WidgetFreeDrag(int x, int y, int w, int h, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, text, source);
    }
    public WidgetFreeDrag(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source) {
        super(x, y, w, h, tv, text, source);
    }

    @Override
    public void attemptDrag(int mouseX, int mouseY) {
        if(draggedWidget == null || draggedWidget == this) {
            if (hasBeenClicked && isHovered()){
                translate(mouseX - mouseStartX, mouseY - mouseStartY);
                mouseStartX = mouseX;
                mouseStartY = mouseY;
                draggedWidget = this;
            }
        }
    }

    @Override
    public boolean isHovered() {
        if(parent.getHoveredSlot() != null) return false;
        return super.isHovered();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(super.mouseClicked(mouseX,mouseY,button)) {
            if (button == 0 && isHovered()) {
                hasBeenClicked = true;
                if (!dragStarted) {
                    mouseStartX = (int) mouseX;
                    mouseStartY = (int) mouseY;
                    dragStarted = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        hasBeenClicked = false;
        dragStarted = false;
        draggedWidget = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    public WidgetFreeDrag getUpdatedWidget(){
        WidgetFreeDrag updated = new WidgetFreeDrag(x, y, width, height, startPos, getMessage(), source);
        updated.children = updateChildren();
        return updated;
    }
}
