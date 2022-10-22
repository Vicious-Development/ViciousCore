package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import com.vicious.viciouscore.common.data.structures.SyncableIVCNBT;
import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import com.vicious.viciouscore.common.inventory.SlotChangedEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SyncableInventory extends SyncableIVCNBT<FastItemStackHandler> implements IFastItemHandler {
    public SyncableInventory(String key, int size) {
        super(key, new FastItemStackHandler(size));
        value.listenChanged(this::listenInv);
    }
    protected void listenInv(SlotChangedEvent ev){
        isDirty(true);
    }

    public FastItemStackHandler addSlotValidator(int slot, Predicate<ItemStack> pred){
        return value.addSlotValidator(slot,pred);
    }

    @Override
    public boolean contains(ItemStack stack) {
        return value.contains(stack);
    }

    @Override
    public ItemStack swap(int slot, ItemStack stack) {
        return value.swap(slot,stack);
    }

    @Override
    public ItemStack extractItem(ItemStack requested, boolean simulate) {
        return value.extractItem(requested,simulate);
    }

    @Override
    public ItemStack insertItem(ItemStack push, boolean simulate) {
        return value.insertItem(push,simulate);
    }

    @Override
    public Collection<Integer> indexOf(ItemStack stack) {
        return value.indexOf(stack);
    }

    @Override
    public void listenChanged(Consumer<SlotChangedEvent> cons) {
        value.listenChanged(cons);
    }

    @Override
    public void stopListening(Consumer<SlotChangedEvent> cons) {
        value.listenChanged(cons);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return value.getItems();
    }

    @Override
    public Container asContainer() {
        return value.asContainer();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean mayPlace(int slot, ItemStack stack) {
        return value.mayPlace(slot,stack);
    }

    @Override
    public Collection<Consumer<SlotChangedEvent>> getListeners() {
        return value.getListeners();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        value.setStackInSlot(slot,stack);
    }

    @Override
    public int getSlots() {
        return value.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return value.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return value.insertItem(slot,stack,simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return value.extractItem(slot,amount,simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return value.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return value.isItemValid(slot,stack);
    }
}
