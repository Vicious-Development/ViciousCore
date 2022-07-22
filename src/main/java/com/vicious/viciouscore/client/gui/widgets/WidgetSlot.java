package com.vicious.viciouscore.client.gui.widgets;


import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class WidgetSlot extends WidgetNoInteraction {
    private ZESlot linkedSlot;
    private GenericContainer container;
    public WidgetSlot(int x, int y, int w, int h, TextComponent text, ResourceLocation source, ZESlot slot, GenericContainer inv) {
        super(x, y, w, h, text, source);
        linkedSlot = slot;
        container = inv;
    }

    public WidgetSlot(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source, ZESlot slot, GenericContainer inv) {
        super(x, y, w, h, tv, text, source);
        linkedSlot = slot;
        container = inv;
    }
    public ArrayList<VCWidget> getChildren(){
        return children;
    }
    protected void updatePosition(){
        x += translationVector.x;
        y += translationVector.y;
        ZESlot newSlot = linkedSlot.moveSlot(linkedSlot.xPos + translationVector.x, linkedSlot.yPos + translationVector.y);
        container.overwriteSlot(linkedSlot, newSlot);
        linkedSlot = newSlot;
        int index = linkedSlot.getSlotIndex() + linkedSlot.getRange().firstIndex;
        if(index != -1) container.inventorySlots.set(index, newSlot);
    }
    public WidgetSlot getUpdatedWidget(){
        WidgetSlot updated = new WidgetSlot(x, y, width, height, startPos, getMessage(), source, linkedSlot, container);
        updated.children = updateChildren();
        return updated;
    }
}
