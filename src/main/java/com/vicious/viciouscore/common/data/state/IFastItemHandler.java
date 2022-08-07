package com.vicious.viciouscore.common.data.state;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IFastItemHandler {
    boolean contains(ItemStack stack);
    ItemStack extractItem(ItemStack requested, boolean simulate);
    ItemStack insertItem(ItemStack push, boolean simulate);
    ItemStack forceInsertItem(ItemStack push, boolean simulate);
    @NotNull ItemStack forceInsertItem(int slot, @NotNull ItemStack stack, boolean simulate);
    void listenChanged(Consumer<IFastItemHandler> cons);
    void stopListening(Consumer<IFastItemHandler> cons);
    void onUpdate();
    boolean mayPlace(int slot, ItemStack stack);
}
