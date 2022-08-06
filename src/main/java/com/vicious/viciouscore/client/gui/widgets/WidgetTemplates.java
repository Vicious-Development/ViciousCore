package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class WidgetTemplates {
    public WidgetSlot generateSlotWidget(RootWidget root, int x, int y, TextureAtlasSprite.Info slotImage, InventoryWrapper wrapper, int slot){
        int w = slotImage.width();
        int h = slotImage.height()
        WidgetItem itemWidget = new WidgetItem(root,1,1,w-2,h-2,()->wrapper.getItem(slot));
        WidgetSlot slotWidget = new WidgetSlot(root,x,y,w,h,slotImage.name(),wrapper,slot);
        slotWidget.addChild(itemWidget);
        return slotWidget;
    }
}
