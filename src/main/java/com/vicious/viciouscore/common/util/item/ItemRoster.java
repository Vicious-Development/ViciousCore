package com.vicious.viciouscore.common.util.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * IMPORTANT INFORMATION!
 * Stacks stored within this are UNCLONED!
 *
 * Why?
 *
 * Due to a risk that NBT data may change the in stack it is imperative that the stacks are references to the same memory as the stacks stored within the inventory.
 * This is still a faster method of determining if a stack is within an inventory than cycling through the entire thing
 */
public class ItemRoster {
    private final Map<Item, TypeStack> stacks = new HashMap<>();
    public void add(ItemStack stack){
        if(stack.isEmpty()) return;
        getStacksOf(stack).add(stack);
    }
    public void remove(ItemStack stack){
        if(stack.isEmpty()) return;
        if(stacks.containsKey(stack.getItem())){
            TypeStack typeStack = stacks.get(stack.getItem());
            typeStack.remove(stack);
            if(typeStack.isEmpty()){
                stacks.remove(stack.getItem());
            }
        }
    }
    public boolean contains(ItemStack stack){
        return getTotalOf(stack) >= stack.getCount();
    }
    public boolean hasAtLeast(ItemStack stack){
        if(stack.isEmpty()) return true;
        if(stacks.containsKey(stack.getItem())){
            return stacks.get(stack.getItem()).hasAtLeast(stack);
        }
        return false;
    }
    public int getTotalOf(ItemStack stack){
        if(!stack.isEmpty()){
            if(stacks.containsKey(stack.getItem())){
                return stacks.get(stack.getItem()).getTotalOf(stack);
            }
        }
        return 0;
    }

    public TypeStack getStacksOf(ItemStack stack){
        return stacks.computeIfAbsent(stack.getItem(),(k)->new TypeStack());
    }

    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    private static class TypeStack{
        private final List<ItemStack> stacks = new ArrayList<>();
        public void add(ItemStack stack){
            //DO NOT CLONE! NBT CAN CHANGE!
            stacks.add(stack);
        }
        public void remove(ItemStack stack){
            for (int i = 0; i < stacks.size(); i++) {
                ItemStack s = stacks.get(i);
                if(s == stack){
                    stacks.remove(i);
                    return;
                }
            }
        }

        public boolean isEmpty() {
            return stacks.isEmpty();
        }

        public boolean hasAtLeast(ItemStack stack){
            int count = stack.getCount();
            for (ItemStack itemStack : stacks) {
                if(ItemHelper.doItemsMatch(itemStack,stack)){
                    count-=itemStack.getCount();
                    if(count <= 0){
                        return true;
                    }
                }
            }
            return false;
        }

        public int getTotalOf(ItemStack stack) {
            int count = 0;
            for (ItemStack itemStack : stacks) {
                if(ItemHelper.doItemsMatch(itemStack,stack)) {
                    count += itemStack.getCount();
                }
            }
            return count;
        }
    }
}
