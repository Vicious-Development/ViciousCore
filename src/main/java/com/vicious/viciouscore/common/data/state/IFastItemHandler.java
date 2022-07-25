package com.vicious.viciouscore.common.data.state;

import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface IFastItemHandler {
    boolean contains(ItemStack stack);
    ItemStack extractItem(ItemStack requested, boolean simulate);
    ItemStack insertItem(ItemStack push, boolean simulate);
    void listenChanged(Consumer<IFastItemHandler> cons);
    void stopListening(Consumer<IFastItemHandler> cons);
    void onUpdate();
}
