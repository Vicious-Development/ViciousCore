package com.vicious.viciouscore.common.util.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class InventoryIndexRoster extends ItemRoster {
    private Map<Item,StackSlot> map = new HashMap<>();

    public void add(ItemStack stack, int slot){
        if(stack.isEmpty()) return;
        getSlots(stack).add(stack,slot);
    }

    public void remove(ItemStack stack, int slot){
        if(stack.isEmpty()) return;
        if(map.containsKey(stack.getItem())){
            map.get(stack.getItem()).remove(stack,slot);
        }
    }

    public Set<Integer> getSlotsContaining(ItemStack stack){
        if(map.containsKey(stack.getItem())){
            map.get(stack.getItem()).getSlotsOf(stack);
        }
        return new HashSet<>();
    }

    protected StackSlot getSlots(ItemStack stack){
        return map.computeIfAbsent(stack.getItem(),(i)->new StackSlot());
    }

    private static class StackSlot{
        private final Set<TaggedStackSlot> slots = new HashSet<>();

        public void add(ItemStack stack, int slot){
            slots.add(new TaggedStackSlot(stack,slot));
        }
        public void remove(ItemStack stack, int slot){
            slots.remove(new TaggedStackSlot(stack,slot));
        }
        public Set<Integer> getSlotsOf(ItemStack stack){
            Set<Integer> ints = new HashSet<>();
            for (TaggedStackSlot slot : slots) {
                if(slot.matches(stack)){
                    ints.add(slot.getSlot());
                }
            }
            return ints;
        }
    }

    private static class TaggedStackSlot{
        private final ItemStack stack;
        private final int slot;
        public TaggedStackSlot(ItemStack stack, int slot){
            this.slot=slot;
            this.stack=stack;
        }
        public boolean matches(ItemStack other){
            return ItemHelper.doItemsMatch(other,stack);
        }
        public int getSlot(){
            return slot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TaggedStackSlot that = (TaggedStackSlot) o;
            return slot == that.slot;
        }

        @Override
        public int hashCode() {
            return Objects.hash(slot);
        }
    }
}
