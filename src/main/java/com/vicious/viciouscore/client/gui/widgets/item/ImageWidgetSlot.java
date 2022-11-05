package com.vicious.viciouscore.client.gui.widgets.item;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.*;
import com.vicious.viciouscore.common.inventory.container.InventoryWrapper;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import net.minecraft.resources.ResourceLocation;

public class ImageWidgetSlot<T extends ImageWidgetSlot<T>> extends WidgetImage<T> implements ISlotWidget<T> {
    protected InventoryWrapper<?> wrapper;
    protected ResourceLocation selectedImage;
    protected WidgetItem<?> item;
    protected int slot;
    public ImageWidgetSlot(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource, InventoryWrapper<?> wrapper, int slot) {
        super(root, x, y, w, h, widgetResource);
        this.wrapper=wrapper;
        this.slot=slot;
        addFlags(ControlFlag.RESPONDTOHOVER);
        addFlags(ControlFlag.RESPONDTOCLICK);
        item = addChild(new WidgetItem<>(root,0,0,0,0,()->wrapper.getItem(slot))).removeFlags(ControlFlag.RESPONDTORAYTRACE);

    }
    public ImageWidgetSlot<?> setSelectedImage(ResourceLocation rl){
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

    @Override
    public T clone(int x, int y, int w, int h, InventoryWrapper<?> wrapper, int slot) {
        ImageWidgetSlot<T> newWidget = new ImageWidgetSlot<>(root,x,y,w,h,selectedImage,wrapper,slot);
        item.copyGLsTo(newWidget.item);
        copyGLsTo(newWidget);
        return newWidget.asT();
    }
}
