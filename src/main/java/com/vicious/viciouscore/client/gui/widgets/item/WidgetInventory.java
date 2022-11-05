package com.vicious.viciouscore.client.gui.widgets.item;

import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.client.util.Extents;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;

public class WidgetInventory<T extends WidgetInventory<T,S>, S extends VCWidget<S>> extends VCWidget<T> {
    protected int numColumns = 9;
    protected InventoryWrapper<?> wrapper;
    public ISlotWidget<S> baseSlot;
    public WidgetInventory(RootWidget root, int x, int y, int w, int h, InventoryWrapper<?> wrapper, ISlotWidget<S> baseSlot) {
        super(root, x, y, w, h);
        this.wrapper=wrapper;
        this.baseSlot=baseSlot;
        generate();
    }
    public WidgetInventory(RootWidget root, int x, int y, int w, int h, InventoryWrapper<?> wrapper, int numColumns, ISlotWidget<S> baseSlot) {
        super(root, x, y, w, h);
        this.wrapper=wrapper;
        this.baseSlot=baseSlot;
        if(numColumns < 1) {
            throw new IllegalArgumentException("An inventory widget cannot render an inventory with 0 columns.");
        }
        this.numColumns = numColumns;
        generate();
    }
    protected void generate(){
        int clickableSize = 16;
        for (int i = 0; i < wrapper.getSize(); i++) {
            int row = i/numColumns;
            int column = i-row*numColumns;
            S slot = baseSlot.clone(column*(clickableSize+1),row*(clickableSize+1),clickableSize,clickableSize,wrapper,i);
            addChild(slot);
        }
        regenerateExtents();
    }

    protected void regenerateExtents(){
        Extents e = getDescendantExtents();
        this.setWidth(e.getWidth());
        this.setHeight(e.getHeight());
    }
}
