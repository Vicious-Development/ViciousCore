package com.vicious.viciouscore.common.util.item;


import com.vicious.viciouscore.common.util.item.types.metaless.TaglessItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Acts as an ItemStackMap ignoring NBT.
 */
public class RoughItemStackMap extends HashMap<TaglessItem<?>, ItemStack> {
    public ItemStack get(ItemStack basis){
        return get(basis.getItem());
    }
    /**
     * @param stack the stack to add.
     * @return if the Material was already present.
     */
    public boolean add(ItemStack stack){
        TaglessItem<?> mat = TaglessItem.fromItemStack(stack);
        if(putIfAbsent(mat,stack.copy()) != null){
            ItemStack mappedStack = get(mat);
            mappedStack.setCount(mappedStack.getCount()+stack.getCount());
            replace(mat,mappedStack);
            return true;
        }
        return false;
    }

    /**
     * Reduces a stored item
     * @param stack
     * @return
     */
    public boolean reduceBy(ItemStack stack){
        TaglessItem<?> mat = TaglessItem.fromItemStack(stack);
        ItemStack mappedStack = get(mat);
        if(mappedStack == null) return false;
        mappedStack.setCount(mappedStack.getCount()-stack.getCount());
        if(mappedStack.getCount() == 0) remove(mat);
        return mappedStack.getCount() < 0;
    }

    public RoughItemStackMap addAll(Collection<ItemStack> items){
        for (ItemStack item : items) {
            add(item);
        }
        return this;
    }
    public RoughItemStackMap addFromEntities(ItemEntity... itemEntities) {
        for (ItemEntity item : itemEntities) {
            ItemStack stack = item.getItem();
            add(stack);
        }
        return this;
    }
    public List<ItemStack> getStacks(){
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack value : values()) {
            if(value.getCount() <= 0) continue;
            convertToLegalStacks(stacks,value);
        }
        return stacks;
    }
    private RoughItemStackMap convertToLegalStacks(List<ItemStack> toAddTo, ItemStack value){
        int maxStackCount = value.getCount()/value.getMaxStackSize();
        int modular = value.getCount()%value.getMaxStackSize();
        if(maxStackCount > 0){
            for (int i = 0; i < maxStackCount; i++) {
                ItemStack toAdd = new ItemStack(value.getItem(),value.getMaxStackSize());
                toAdd.setTag(value.getTag());
                toAddTo.add(toAdd);
            }
        }
        if(modular != 0){
            ItemStack toAdd = new ItemStack(value.getItem(),modular);
            toAdd.setTag(value.getTag());
            toAddTo.add(toAdd);
        }
        return this;
    }

    public RoughItemStackMap combine(RoughItemStackMap stacks) {
        for (ItemStack value : stacks.values()) {
            add(value);
        }
        return this;
    }

    /**
     * Checks if the map contains AT LEAST all the elements in the list.
     */
    public boolean hasAll(List<ItemStack> stacks){
        //Map to unify stacks.
        RoughItemStackMap mapped = new RoughItemStackMap().addAll(stacks);
        for (TaglessItem<?> k : mapped.keySet()) {
            ItemStack mapStack = get(k);
            if (mapStack == null) return false;
            ItemStack expected = mapped.get(k);
            if (mapStack.getCount() >= expected.getCount()) mapped.remove(k);
            else mapped.reduceBy(mapStack);
        }
        //Mapped should be empty by the end of this.
        return mapped.size() == 0;
    }

    /**
     * WARNING THIS IS A LOSSY PROCESS!
     * Converting between rough and fine itemstackmaps eliminates NBT regardless and the resulting fine map will still act just like the rough map until new items are added.
     * @return
     */
    public ItemStackMap asFineMap(){
        return new ItemStackMap().addAll(values());
    }
}
