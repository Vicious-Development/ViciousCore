package com.vicious.viciouscore.client.gui.widgets.item;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.vicious.viciouscore.client.gui.widgets.*;
import com.vicious.viciouscore.client.gui.widgets.glrendered.WidgetRectangle;
import com.vicious.viciouscore.client.gui.widgets.glrendered.WidgetRectangularBorder;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;

import java.awt.*;

public class GLWidgetSlot<T extends GLWidgetSlot<T>> extends VCWidget<T> implements ISlotWidget<T> {
    protected WidgetRectangle<?> backing;
    protected WidgetRectangularBorder<?> border;
    protected WidgetItem<?> item;
    protected InventoryWrapper<?> wrapper;
    protected int slot;
    public GLWidgetSlot(RootWidget root, int x, int y, int w, int h, InventoryWrapper<?> wrapper, int slot) {
        super(root, x, y, w, h);
        this.wrapper = wrapper;
        this.slot = slot;
        backing = addChild(new WidgetRectangle<>(root, x, y, w, h).addGL(RenderStage.SELFPRE, (s) -> {
            Color c = Color.GREEN;
            RenderSystem.enableBlend();
            if(hasFlag(ControlFlag.HOVERED)){
                c = c.brighter();
            }
            RenderSystem.setShaderColor(c.getRed(), c.getGreen(), c.getBlue(), 0.5f);
        })).addGL(RenderStage.SELFPOST,(s)->RenderSystem.disableBlend());
        border = backing.addChild(new WidgetRectangularBorder<>(root, x - 1, y - 1, w + 2, h + 2, 1)).addGL(RenderStage.SELFPRE, (s) -> {
            Color c = Color.DARK_GRAY;
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(c.getRed(), c.getGreen(), c.getBlue(), 0.25f);
        }).addGL(RenderStage.SELFPOST,(s)->RenderSystem.disableBlend());
        item = backing.addChild(new WidgetItem<>(root,0,0,0,0,()->wrapper.getItem(slot))).removeFlags(ControlFlag.RESPONDTORAYTRACE);
        addFlags(ControlFlag.RESPONDTOHOVER,ControlFlag.RESPONDTOCLICK);
    }

    @Override
    public VCWidget<?> widgetMouseOver() {
        addFlags(ControlFlag.HOVERED);
        return this;
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
