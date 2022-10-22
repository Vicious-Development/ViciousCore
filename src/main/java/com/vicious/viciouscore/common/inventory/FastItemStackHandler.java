package com.vicious.viciouscore.common.inventory;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import com.vicious.viciouscore.common.util.item.ItemHelper;
import com.vicious.viciouscore.common.util.item.ItemSlotMap;
import com.vicious.viciouscore.common.util.item.ItemStackMap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FastItemStackHandler implements IFastItemHandler, IVCNBTSerializable {
    protected final ItemStackMap map = new ItemStackMap();
    protected final ItemSlotMap slotMemory = new ItemSlotMap();
    protected final Map<Integer,Predicate<ItemStack>> validators = new HashMap<>();
    protected final List<Consumer<SlotChangedEvent>> changeListeners = new ArrayList<>();
    protected NonNullList<ItemStack> stacks;

    public FastItemStackHandler(int size){
        stacks = NonNullList.withSize(size,ItemStack.EMPTY);
    }
    public FastItemStackHandler(NonNullList<ItemStack> stacks){
        this.stacks=stacks;
    }

    public FastItemStackHandler addSlotValidator(int slot, Predicate<ItemStack> pred){
        validators.put(slot,pred);
        return this;
    }

    @Override
    public Set<Integer> indexOf(ItemStack stack) {
        Set<Integer> out = slotMemory.get(stack);
        if(out == null) return Set.of();
        else{
            return new HashSet<>(out);
        }
    }

    @Override
    public boolean contains(ItemStack stack) {
        return indexOf(stack).isEmpty();
    }

    @Override
    public ItemStack swap(int slot, ItemStack swap) {
        ItemStack original = getStackInSlot(slot);
        setStackInSlot(slot,swap);
        return original;
    }

    @Override
    public ItemStack extractItem(ItemStack requested, boolean simulate) {
        Set<Integer> locations = indexOf(requested);
        ItemStack out = requested.copy();
        int remainingToPull = requested.getCount();
        if(!locations.isEmpty()){
            for (Integer index : locations) {
                ItemStack extracted = extractItem(index,remainingToPull,simulate);
                remainingToPull = Math.max(0,remainingToPull-extracted.getCount());
                if(remainingToPull == 0){
                    return out;
                }
            }
        }
        out.shrink(remainingToPull);
        return out;
    }

    @Override
    public ItemStack insertItem(ItemStack push, boolean simulate){
        Set<Integer> locations = indexOf(push);
        ItemStack out = push.copy();
        if(!locations.isEmpty()){
            for (Integer index : locations) {
                out = insertItem(index,out,simulate);
                if(out.isEmpty()){
                    return ItemStack.EMPTY;
                }
            }
        }
        for (int i = 0; i < getSlots(); i++) {
            out = insertItem(i,out,simulate);
            if(out.isEmpty()){
                return ItemStack.EMPTY;
            }
        }
        return out;
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        validateInRange(slot);
        ItemStack inserted = getStackInSlot(slot);
        int toInsert = Math.min(stack.getCount(),ItemHelper.getStackSpaceRemaining(inserted));
        inserted = inserted.copy();
        inserted.grow(toInsert);
        if(!simulate) {
            setStackInSlot(slot, inserted);
        }
        stack = stack.copy();
        stack.shrink(toInsert);
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        validateInRange(slot);
        ItemStack stack = getStackInSlot(slot);
        ItemStack clone = stack.copy();
        int toPull = Math.min(stack.getCount(),amount);
        if(!simulate){
            sendEventPre(slot, SlotChangedEvent.Action.EXTRACT);
            shrinkMaps(slot,toPull);
            stack.shrink(toPull);
            sendEventPost(slot, SlotChangedEvent.Action.EXTRACT);
        }
        clone.setCount(toPull);
        return clone;
    }
    private void growMaps(int slot, int toPush){
        ItemStack og = getStackInSlot(slot);
        ItemStack grow = og.copy();
        grow.setCount(toPush);
        map.add(grow);
        slotMemory.add(grow,slot);
    }

    private void shrinkMaps(int slot, int toPull) {
        ItemStack og = getStackInSlot(slot);
        ItemStack shrink = og.copy();
        shrink.setCount(toPull);
        map.reduceBy(shrink);
        if(og.getCount()-shrink.getCount() <= 0){
            slotMemory.remove(og,slot);
        }
    }

    private void validateInRange(int slot) {
        if(slot < 0 || slot >= stacks.size()){
            throw new IndexOutOfBoundsException(slot + " is not within range: [0" + "," + stacks.size() + ")");
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return stacks.size();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return validators.get(slot).test(stack);
    }

    @Override
    public void listenChanged(Consumer<SlotChangedEvent> cons) {
        changeListeners.add(cons);
    }

    @Override
    public void stopListening(Consumer<SlotChangedEvent> cons) {
        changeListeners.add(cons);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return stacks;
    }

    @Override
    public Container asContainer() {
        return new FastItemHandlerContainer(this);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean mayPlace(int slot, ItemStack stack) {
        return validators.get(slot).test(stack);
    }

    @Override
    public Collection<Consumer<SlotChangedEvent>> getListeners() {
        return changeListeners;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        ItemStack og = getStackInSlot(slot);
        shrinkMaps(slot,og.getCount());
        stacks.set(slot,stack);
        growMaps(slot,stack.getCount());
    }

    @Override
    public void serializeNBT(CompoundTag tag, DataAccessor destination) {
        CompoundTag out = new CompoundTag();
        CompoundTag items = new CompoundTag();
        for (int i = 0; i < stacks.size(); i++) {
            CompoundTag item = new CompoundTag();
            ItemStack stack = stacks.get(i);
            if(!stack.isEmpty()) stack.save(item);
            items.put(""+i,item);
        }
        out.put("items",items);
        out.putInt("size",stacks.size());
        tag.put("inv",out);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        tag = tag.getCompound("inv");
        stacks = NonNullList.withSize(tag.getInt("size"),ItemStack.EMPTY);
        tag = tag.getCompound("items");
        for (String key : tag.getAllKeys()) {
            CompoundTag itemTag = tag.getCompound(key);
            setStackInSlot(Integer.parseInt(key),ItemStack.of(itemTag));
        }
    }
}

