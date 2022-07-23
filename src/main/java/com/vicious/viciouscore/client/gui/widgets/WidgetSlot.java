package com.vicious.viciouscore.client.gui.widgets;


import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import com.vicious.viciouscore.common.inventory.slots.VCSlot;
import net.minecraft.resources.ResourceLocation;

public class WidgetSlot extends WidgetNoInteraction {
    private VCSlot linkedSlot;
    private final GenericContainer<?> container;
    public WidgetSlot(RootWidget root, int x, int y, int w, int h, ResourceLocation source, VCSlot slot, GenericContainer<?> inv) {
        super(root,x,y,w,h,source);
        linkedSlot = slot;
        container = inv;
    }

    @Override
    public void calculateVectors() {
        super.calculateVectors();
        VCSlot newSlot = linkedSlot.moveSlot(actualPosition.x,actualPosition.y);
        container.overwriteSlot(linkedSlot, newSlot);
        linkedSlot = newSlot;
        int index = linkedSlot.getSlotIndex() + linkedSlot.getRange().firstIndex;
        if(index != -1) container.slots.set(index, newSlot);
    }
}
