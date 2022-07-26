package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SyncableInventory extends SyncableINBTCompound<FastItemStackHandler> implements IItemHandler, IFastItemHandler {
    public SyncableInventory(String key, int size) {
        super(key, new FastItemStackHandler(size));
        value.listenChanged(this::listenInv);
    }

    @Override
    public <V extends SyncableValue<FastItemStackHandler>> V readRemote(boolean readRemote) {
        readRemote=false;
        return (V) this;
    }

    protected void listenInv(IFastItemHandler cons){
        isDirty(true);
    }
    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.FASTITEMSTACKHANDLER, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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

    @Override
    public boolean contains(ItemStack stack) {
        return value.contains(stack);
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
    public void listenChanged(Consumer<IFastItemHandler> cons) {
        value.listenChanged(cons);
    }

    @Override
    public void stopListening(Consumer<IFastItemHandler> cons) {
        value.listenChanged(cons);
    }

    @Override
    public void onUpdate() {
        value.onUpdate();
    }
}