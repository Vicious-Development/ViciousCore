package com.vicious.viciouscore.client.gui.widgets.item;

import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;

public interface ISlotWidget<T extends VCWidget<T>> {
    T clone(int x, int y, int w, int h, InventoryWrapper<?> wrapper, int slot);
}
