package com.vicious.viciouscore.common.capability.combined;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CombinedItemHandler implements ICombinedCapabilityProvider<IItemHandler>,IItemHandler{
    private final List<IItemHandler> handlers = new ArrayList<>();
    private int slots;

    public CombinedItemHandler(){}
    public CombinedItemHandler(IItemHandler... handlers){
        for (IItemHandler handler : handlers) {
            this.handlers.add(handler);
        }
    }
    @Override
    public List<IItemHandler> getProviders() {
        return handlers;
    }

    @Override
    public void add(IItemHandler handler) {
        handlers.add(handler);
        slots+=handler.getSlots();
    }

    @Override
    public void remove(IItemHandler handler) {
        if(handlers.remove(handler)){
            slots-=handler.getSlots();
        }
    }
    private final LazyOptional<ICombinedCapabilityProvider<IItemHandler>> lop = LazyOptional.of(()->this);

    @Override
    public LazyOptional<ICombinedCapabilityProvider<IItemHandler>> getLazyOptional() {
        return lop;
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        Tuple<IItemHandler, Integer> pair = getTrueSlot(slot);
        if(pair == null) return ItemStack.EMPTY;
        else return pair.getA().getStackInSlot(pair.getB());
    }
    public Tuple<IItemHandler,Integer> getTrueSlot(int slot){
        for (int i = 0; i < handlers.size(); i++) {
            IItemHandler handler = handlers.get(i);
            if(slot > handler.getSlots()){
                slot-=handler.getSlots();
            }
            else{
                return new Tuple<>(handler,slot);
            }
        }
        return null;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        Tuple<IItemHandler, Integer> pair = getTrueSlot(slot);
        if(pair == null) return stack.copy();
        else return pair.getA().insertItem(pair.getB(),stack,simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        Tuple<IItemHandler, Integer> pair = getTrueSlot(slot);
        if(pair == null) return ItemStack.EMPTY;
        else return pair.getA().extractItem(pair.getB(),amount,simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return getStackInSlot(slot).getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        Tuple<IItemHandler, Integer> pair = getTrueSlot(slot);
        if(pair == null) return false;
        else return pair.getA().isItemValid(pair.getB(),stack);
    }
}
