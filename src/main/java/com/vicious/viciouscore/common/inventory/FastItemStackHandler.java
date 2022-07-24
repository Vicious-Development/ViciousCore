package com.vicious.viciouscore.common.inventory;

import com.vicious.viciouscore.common.util.item.ItemSlotMap;
import com.vicious.viciouscore.common.util.item.ItemStackMap;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FastItemStackHandler extends ItemStackHandler {
    protected ItemStackMap map = new ItemStackMap();
    protected ItemSlotMap slotMemory = new ItemSlotMap();
    protected List<Consumer<FastItemStackHandler>> changeListeners = new ArrayList<>();
    public FastItemStackHandler()
    {
        this(1);
    }

    public FastItemStackHandler(int size)
    {
        super(size);
    }

    public FastItemStackHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        ItemStack before = getStackInSlot(slot);
        this.stacks.set(slot,stack);
        slotMemory.add(stack,slot);
        map.reduceBy(before);
        map.add(stack);
        onContentsChanged(slot);
        onUpdate();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack remaining = super.insertItem(slot, stack, simulate);
        if(!simulate) {
            if (remaining.getCount() < stack.getCount()) {
                ItemStack addStack = stack.copy();
                addStack.setCount(stack.getCount() - remaining.getCount());
                map.add(addStack);
                slotMemory.add(stack,slot);
                onUpdate();
            }
        }
        return remaining;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extracted = super.extractItem(slot,amount,simulate);
        if(!simulate){
            if(!extracted.isEmpty()){
                map.reduceBy(extracted);
                if(stacks.get(slot).isEmpty()){
                    slotMemory.remove(extracted,slot);
                }
                onUpdate();
            }
        }
        return extracted;
    }


    /**
     * @param stack the stack to find.
     * @return if the map contains the item with a count equal or greater to the stacksize.
     */
    public boolean contains(ItemStack stack){
        return map.get(stack).getCount() >= stack.getCount();
    }

    /**
     * Looks in known slots for the requested item. If the item is not there, then ends. Much quicker than your average item extraction algorithm.
     *
     * @param requested the ItemStack to pull.
     * @param simulate whether to actually pull the stack.
     * @return the extracted stack with count less than or equal to requested.
     */
    public ItemStack extractItem(ItemStack requested, boolean simulate){
        List<Integer> possibleSlots = slotMemory.get(requested);
        int toRemove = requested.getCount();
        for (int i = 0; i < possibleSlots.size(); i++) {
            if(toRemove == 0) break;
            int index = possibleSlots.get(i);
            ItemStack stack = stacks.get(index);
            if(stack.isEmpty()){
                possibleSlots.remove(i);
                i--;
            }
            else{
                int remaining = Math.max(stack.getCount()-toRemove,0);
                if(!simulate){
                    if(remaining != 0){
                        stack.setCount(remaining);
                    }
                    else{
                        possibleSlots.remove(i);
                        i--;
                        stacks.set(index,ItemStack.EMPTY);
                    }
                    onContentsChanged(index);
                }
                toRemove-=toRemove-remaining;
            }
        }
        requested.setCount(requested.getCount()-toRemove);
        if(requested.getCount() > 0){
            onUpdate();
            return requested;
        }
        else return ItemStack.EMPTY;
    }
    /**
     * Looks in known slots for the requested item. If the item is not there, then finds the next slot containing it or an empty slot.
     *
     * @param push the ItemStack to push.
     * @param simulate whether to actually push the stack.
     * @return the extracted stack with count less than or equal to requested.
     */
    public ItemStack insertItem(ItemStack push, boolean simulate){
        ItemStack pushClone = push.copy();
        List<Integer> prioritySlots = slotMemory.get(push);
        int remaining = push.getCount();
        for (int i = 0; i < prioritySlots.size(); i++) {
            if(remaining == 0) break;
            int index = prioritySlots.get(i);
            ItemStack stack = stacks.get(index);
            if(stack.isEmpty()){
                prioritySlots.remove(i);
                i--;
            }
            else{
                if(stack.getMaxStackSize() < stack.getCount()){
                    int toInsert = Math.min(stack.getMaxStackSize()-stack.getCount(),remaining);
                    remaining-=toInsert;
                    if(!simulate){
                        stack.setCount(toInsert+stack.getCount());
                        onContentsChanged(index);
                    }
                }
            }
        }
        push.setCount(remaining);
        for (int i = 0; i < stacks.size(); i++) {
            remaining = push.getCount();
            if(push.getCount() == 0) return ItemStack.EMPTY;
            push = insertItem(i,push,simulate);
            if(push.getCount() < remaining){
                slotMemory.add(pushClone,i);
            }
        }
        if(pushClone.getCount() != push.getCount()){
            onUpdate();
        }
        return push;
    }

    public void listenChanged(Consumer<FastItemStackHandler> cons){
        changeListeners.add(cons);
    }
    public void stopListening(Consumer<FastItemStackHandler> cons){
        changeListeners.remove(cons);
    }

    protected void onUpdate() {
        for (Consumer<FastItemStackHandler> changeListener : changeListeners) {
            changeListener.accept(this);
        }
    }
}
