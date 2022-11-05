package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.client.gui.widgets.item.ImageWidgetSlot;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class WidgetTemplates {
    /**
     * @param root = the RootWidget instance
     * @param x = the slot image's x position
     * @param y = the slot image's y position
     * @param slotImage = the texture
     * @param wrapper = the inventory the slot accesses
     * @param slot = the slot the slot is connected to.
     * @return a WidgetSlot with a provided WidgetItem child.
     */
    public static ImageWidgetSlot<?> generateSlotWidget(RootWidget root, int x, int y, TextureAtlasSprite.Info slotImage, InventoryWrapper<?> wrapper, int slot){
        int w = slotImage.width();
        int h = slotImage.height();
        WidgetItem<?> itemWidget = new WidgetItem<>(root,1,1,w-2,h-2,()->wrapper.getItem(slot));
        ImageWidgetSlot<?> slotWidget = new ImageWidgetSlot<>(root,x,y,w,h,slotImage.name(),wrapper,slot);
        slotWidget.addChild(itemWidget);
        return slotWidget;
    }
}
