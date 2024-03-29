package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class WidgetItem<T extends WidgetItem<T>> extends VCWidget<T> {
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
    public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        renderItem();
        super.renderWidget(stack, mouseX, mouseY, partialTicks);
    }
    protected void renderItem() {
        int x = this.actualPosition.x;
        int y = this.actualPosition.y;
        ItemStack stack = supplier.get();
        if(!stack.isEmpty()){
            if(renderNumbers) {
                renderer.renderAndDecorateItem(stack,x,y);
                renderer.renderGuiItemDecorations(Minecraft.getInstance().font, stack, x, y);
            }
            else {
                renderer.renderGuiItem(stack, x, y);
            }
        }
    }
    protected boolean renderNumbers = true;

    public T renderNumbers(boolean v){
        this.renderNumbers =v;
        return asT();
    }
}
