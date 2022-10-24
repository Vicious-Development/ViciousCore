package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import net.minecraft.resources.ResourceLocation;

public class WidgetSlot<T extends WidgetSlot<T>> extends WidgetImage<T>{
    protected InventoryWrapper<?> wrapper;
    protected ResourceLocation selectedImage;
    protected int slot;
    public WidgetSlot(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource, InventoryWrapper<?> wrapper, int slot) {
        super(root, x, y, w, h, widgetResource);
        this.wrapper=wrapper;
        this.slot=slot;
    }
    public WidgetSlot<?> setSelectedImage(ResourceLocation rl){
        this.selectedImage=rl;
        return this;
    }

    public VCWidget<?> widgetMouseOver(){
        return this;
    }

    @Override
    public void renderWidget(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation post = null;
        if(hasFlag(ControlFlag.HOVERED) && selectedImage != null) {
            post = source;
            source = selectedImage;
        }
        super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
        if(post != null) source=post;
    }

    @Override
    public void onClick(int button) {
        SPacketSlotClicked pkt = new SPacketSlotClicked(slot, (byte) wrapper.index, button,InputConstants.isKeyDown(getWindowID(),InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(getWindowID(),InputConstants.KEY_RSHIFT));
        VCNetwork.getInstance().sendToServer(pkt);
        pkt.handleSelf();
    }

    protected int getYImage(boolean isHovered) {
        return 1;
    }
}
