package com.vicious.viciouscore.common.util.item;

import com.vicious.viciouscore.common.recipe.ingredients.type.ItemTypeKey;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemSlotMap extends ItemTypeMap<List<Integer>> {
    public ItemSlotMap() {
    }
    public void add(ItemStack stack, int slot) {
        ItemTypeKey type = getItemType(stack);
        if (putIfAbsent(type, new ArrayList<>(slot)) == null) return;
        List<Integer> value = get(type);
        if(!value.contains(slot)) value.add(slot);
    }
    public void remove(ItemStack stack, int slot){
        get(stack).remove((Integer) slot);
    }
}
