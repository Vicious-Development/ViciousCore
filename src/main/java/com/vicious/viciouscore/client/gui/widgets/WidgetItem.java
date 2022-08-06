package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class WidgetItem extends VCWidget {
    protected static final ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
    protected Supplier<ItemStack> supplier;
    public WidgetItem(RootWidget root, int x, int y, int w, int h, ItemStack stack) {
        super(root, x, y, w, h);
        this.supplier = ()->stack;
    }
    public WidgetItem(RootWidget root, int x, int y, int w, int h, Supplier<ItemStack> supplier) {
        super(root, x, y, w, h);
        this.supplier = supplier;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderItem();
        super.render(stack, mouseX, mouseY, partialTicks);
    }
    protected void renderItem() {
        int x = this.actualPosition.x;
        int y = this.actualPosition.y;
        renderer.renderGuiItem(supplier.get(),x,y);
    }
}
