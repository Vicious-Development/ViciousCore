package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import net.minecraft.resources.ResourceLocation;

public class WidgetSlot extends WidgetImage{
    protected InventoryWrapper wrapper;
    protected int slot;
    public WidgetSlot(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource, InventoryWrapper wrapper, int slot) {
        super(root, x, y, w, h, widgetResource);
        this.wrapper=wrapper;
        this.slot=slot;

    }

    @Override
    public void onClick(int button) {
        VCNetwork.getInstance().sendToServer(new SPacketSlotClicked(slot, (byte) wrapper.index, button,InputConstants.isKeyDown(getWindowID(),InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(getWindowID(),InputConstants.KEY_RSHIFT)));
    }
}
