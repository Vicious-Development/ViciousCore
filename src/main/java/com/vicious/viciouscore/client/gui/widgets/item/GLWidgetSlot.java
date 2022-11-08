package com.vicious.viciouscore.client.gui.widgets.item;

import com.mojang.blaze3d.platform.InputConstants;
import com.vicious.viciouscore.client.gui.widgets.*;
import com.vicious.viciouscore.client.gui.widgets.glrendered.WidgetRectangle;
import com.vicious.viciouscore.client.gui.widgets.glrendered.WidgetRectangularBorder;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;

public class GLWidgetSlot<T extends GLWidgetSlot<T>> extends VCWidget<T> implements ISlotWidget<T> {
    public WidgetRectangle<?> backing;
    public WidgetRectangularBorder<?> border;
    public WidgetItem<?> item;
    protected InventoryWrapper<?> wrapper;
    protected int slot;
    public GLWidgetSlot(RootWidget root, int x, int y, int w, int h, InventoryWrapper<?> wrapper, int slot) {
        super(root, x, y, w, h);
        this.wrapper = wrapper;
        this.slot = slot;
        backing = addChild(new WidgetRectangle<>(root, x, y, w, h)).onlyVisible();
        border = backing.addChild(new WidgetRectangularBorder<>(root, x - 1, y - 1, w + 2, h + 2, 1)).onlyVisible();
        item = backing.addChild(new WidgetItem<>(root,0,0,0,0,()->wrapper.getItem(slot))).onlyVisible();
        addFlags(ControlFlag.RESPONDTOHOVER,ControlFlag.RESPONDTOCLICK);
    }

    public void onClick(int button) {
        SPacketSlotClicked pkt = new SPacketSlotClicked(this.slot, (byte)this.wrapper.index, button, InputConstants.isKeyDown(this.getWindowID(), 340) || InputConstants.isKeyDown(this.getWindowID(), 344));
        VCNetwork.getInstance().sendToServer(pkt);
        pkt.handleSelf();
    }

    @Override
    public T clone(int x, int y, int w, int h, InventoryWrapper<?> wrapper, int slot) {
        GLWidgetSlot<T> newWidget = new GLWidgetSlot<>(root,x,y,w,h,wrapper,slot);
        backing.copyGLsTo(newWidget.backing);
        border.copyGLsTo(newWidget.border);
        item.copyGLsTo(newWidget.item);
        copyGLsTo(newWidget);
        return newWidget.asT();
    }
}
