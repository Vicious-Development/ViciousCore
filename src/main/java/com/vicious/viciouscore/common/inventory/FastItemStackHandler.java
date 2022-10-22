package com.vicious.viciouscore.common.inventory;

import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import com.vicious.viciouscore.common.util.item.ItemSlotMap;
import com.vicious.viciouscore.common.util.item.ItemStackMap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FastItemStackHandler extends ItemStackHandler implements IFastItemHandler {
    protected ItemStackMap map = new ItemStackMap();
    protected ItemSlotMap slotMemory = new ItemSlotMap();
    protected Map<Integer,Predicate<ItemStack>> validators = new HashMap<>();
    protected List<Consumer<SlotChangedEvent>> changeListeners = new ArrayList<>();
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

    public boolean mayPlace(int slot, ItemStack stack){
        if(validators.containsKey(slot)){
            return validators.get(slot).test(stack);
        }
        return true;
    }

    @Override
    public Collection<Consumer<SlotChangedEvent>> getListeners() {
        return changeListeners;
    }

    public FastItemStackHandler addSlotValidator(int slot, Predicate<ItemStack> pred){
        this.validators.put(slot,pred);
        return this;
    }
    public FastItemStackHandler addValidatorToAllSlots(Predicate<ItemStack> pred){
        for (int i = 0; i < getSlots(); i++) {
            this.validators.put(i,pred);
        }
        return this;
    }

    /**
     * Force places the item in the slot.
     */
    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        ItemStack before = getStackInSlot(slot);
        sendEventPre(slot, SlotChangedEvent.Action.SET);
        this.stacks.set(slot, stack);
        slotMemory.add(stack, slot);
        map.reduceBy(before);
        map.add(stack);
        sendEventPost(slot, SlotChangedEvent.Action.SET);
        onContentsChanged(slot);

    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if(!mayPlace(slot, stack)) return stack.copy();
        else {
            sendEventPre(slot, SlotChangedEvent.Action.INSERT);
            ItemStack ret =  forceInsertItem(slot, stack, simulate);
            sendEventPost(slot, SlotChangedEvent.Action.INSERT);
            return ret;
        }
    }
    @Override
    public @NotNull ItemStack forceInsertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack remaining = super.insertItem(slot, stack, simulate);
        if(!simulate) {
            if (remaining.getCount() < stack.getCount()) {
                ItemStack addStack = stack.copy();
                addStack.setCount(stack.getCount() - remaining.getCount());
                map.add(addStack);
                slotMemory.add(stack,slot);
            }
        }
        return remaining;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        sendEventPre(slot, SlotChangedEvent.Action.EXTRACT);
        ItemStack extracted = super.extractItem(slot,amount,simulate);
        if(!simulate){
            if(!extracted.isEmpty()){
                map.reduceBy(extracted);
                if(stacks.get(slot).isEmpty()){
                    slotMemory.remove(extracted,slot);
                }
            }
        }
        sendEventPost(slot, SlotChangedEvent.Action.EXTRACT);
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
        requested = requested.copy();
        List<Integer> possibleSlots = slotMemory.get(requested);
        int toRemove = requested.getCount();
        if(possibleSlots != null) {
            for (int i = 0; i < possibleSlots.size(); i++) {
                if (toRemove == 0) break;
                int index = possibleSlots.get(i);
                ItemStack stack = stacks.get(index);
                if (stack.isEmpty()) {
                    possibleSlots.remove(i);
                    i--;
                } else {
                    int remaining = Math.max(stack.getCount() - toRemove, 0);
                    if (!simulate) {
                        sendEventPre(index, SlotChangedEvent.Action.EXTRACT);
                        if (remaining != 0) {
                            stack.setCount(remaining);
                        } else {
                            possibleSlots.remove(i);
                            i--;
                            stacks.set(index, ItemStack.EMPTY);
                        }
                        sendEventPost(index, SlotChangedEvent.Action.EXTRACT);
                        onContentsChanged(index);
                    }
                    toRemove -= toRemove - remaining;
                }
            }
        }
        requested.setCount(requested.getCount()-toRemove);
        if(requested.getCount() > 0){
            return requested;
        }
        else return ItemStack.EMPTY;
    }

    /**
     * Looks in known slots for the requested item. If the item is not there, then finds the next slot containing it or an empty slot.
     *
     * @param push the ItemStack to push.
     * @param simulate whether to actually push the stack.
     * @return the inserted stack with count less than or equal to requested.
     */
    public ItemStack insertItem(ItemStack push, boolean simulate){
        ItemStack pushClone = push.copy();
        push = insertPriority(push,simulate);
        int remaining = push.getCount();
        push.setCount(remaining);
        for (int i = 0; i < stacks.size(); i++) {
            remaining = push.getCount();
            if (push.getCount() == 0) return ItemStack.EMPTY;
            push = insertItem(i, push, simulate);
            if (push.getCount() < remaining) {
                slotMemory.add(pushClone, i);
            }
        }
        return push;
    }
    protected ItemStack insertPriority(ItemStack push, boolean simulate){
        push = push.copy();
        List<Integer> prioritySlots = slotMemory.get(push);
        int remaining = push.getCount();
        if(prioritySlots != null) {
            for (int i = 0; i < prioritySlots.size(); i++) {
                if (remaining == 0) break;
                int index = prioritySlots.get(i);
                ItemStack stack = stacks.get(index);
                if (stack.isEmpty()) {
                    prioritySlots.remove(i);
                    i--;
                } else {
                    if (stack.getMaxStackSize() < stack.getCount()) {
                        int toInsert = Math.min(stack.getMaxStackSize() - stack.getCount(), remaining);
                        remaining -= toInsert;
                        if (!simulate) {
                            sendEventPre(index, SlotChangedEvent.Action.INSERT);
                            stack.setCount(toInsert + stack.getCount());
                            sendEventPost(index, SlotChangedEvent.Action.INSERT);
                            onContentsChanged(index);
                        }
                    }
                }
            }
        }
        return push;
    }
    public ItemStack forceInsertItem(ItemStack push, boolean simulate){
        ItemStack pushClone = push.copy();
        push = insertPriority(push,simulate);
        int remaining = push.getCount();
        push.setCount(remaining);
        for (int i = 0; i < stacks.size(); i++) {
            remaining = push.getCount();
            if (push.getCount() == 0) return ItemStack.EMPTY;
            push = forceInsertItem(i, push, simulate);
            if (push.getCount() < remaining) {
                slotMemory.add(pushClone, i);
            }
        }
        return push;
    }

    public void listenChanged(Consumer<SlotChangedEvent> cons){
        changeListeners.add(cons);
    }
    public void stopListening(Consumer<SlotChangedEvent> cons){
        changeListeners.remove(cons);
    }

    public NonNullList<ItemStack> getItems(){
        return stacks;
    }

    public boolean isEmpty(){
        return map.isEmpty();
    }

    public void forEachSlot(Consumer<Integer> cons){
        for (int i = 0; i < getSlots(); i++) {
            cons.accept(i);
        }
    }

    public Container asContainer(){
        return new FastItemHandlerContainer(this);
    }
    public ItemStackMap getMap(){
        return map;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                setStackInSlot(slot,ItemStack.of(itemTags));
                sendEventPost(slot, SlotChangedEvent.Action.SET);
                onContentsChanged(slot);
            }
        }
        onLoad();
    }
}

