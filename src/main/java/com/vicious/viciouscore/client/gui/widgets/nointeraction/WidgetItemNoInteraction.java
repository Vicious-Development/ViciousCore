package com.vicious.viciouscore.client.gui.widgets.nointeraction;

import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.WidgetItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class WidgetItemNoInteraction extends WidgetItem {
    public WidgetItemNoInteraction(RootWidget root, int x, int y, int w, int h, ItemStack stack) {
        super(root, x, y, w, h, stack);
    }

    public WidgetItemNoInteraction(RootWidget root, int x, int y, int w, int h, Supplier<ItemStack> supplier) {
        super(root, x, y, w, h, supplier);
    }

    @Override
    public boolean respondToInputs() {
        return false;
    }
}
