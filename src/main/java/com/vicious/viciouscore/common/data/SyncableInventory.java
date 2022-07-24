package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.values.SyncableInventoryHandler;
import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SyncableInventory extends SidedSyncableData implements IItemHandler {
    public SyncableInventoryHandler handler;
    public SyncableInventory(String name, int size) {
        this.handler = new SyncableInventoryHandler(size, name);
    }

    @Override
    public List<Capability<?>> getCapabilityTokens() {
        return List.of(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, VCCapabilities.FASTITEMSTACKHANDLER);
    }

    @Override
    public int getSlots() {
        return handler.get().getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return handler.get().getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return handler.get().insertItem(slot,stack,simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return handler.get().extractItem(slot,amount,simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.get().getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return handler.get().isItemValid(slot,stack);
    }

    public boolean contains(ItemStack stack){
        return handler.get().contains(stack);
    }

    public ItemStack extractItem(ItemStack requested, boolean simulate){
        return handler.get().extractItem(requested,simulate);
    }

    public ItemStack insertItem(ItemStack push, boolean simulate) {
        return handler.get().insertItem(push,simulate);
    }

    public void listenChanged(Consumer<FastItemStackHandler> cons){
        handler.get().listenChanged(cons);
    }
    public void stopListening(Consumer<FastItemStackHandler> cons){
        handler.get().stopListening(cons);
    }
}
