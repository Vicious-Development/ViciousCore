package com.vicious.viciouscore.common.util.item;

import com.vicious.viciouscore.common.recipe.ingredients.type.ItemTypeKey;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ItemSlotMap extends ItemTypeMap<Set<Integer>> {
    public ItemSlotMap() {
    }
    public void add(ItemStack stack, int slot) {
        ItemTypeKey type = getItemType(stack);
        if (putIfAbsent(type, new HashSet<>(slot)) == null) return;
        Set<Integer> value = get(type);
        value.add(slot);
    }
    public void remove(ItemStack stack, int slot){
        Set<Integer> ints = get(stack);
        if(ints != null) ints.remove(slot);
    }
}
