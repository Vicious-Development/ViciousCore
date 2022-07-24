package com.vicious.viciouscore.common.util.item;

import com.vicious.viciouscore.common.util.item.types.ItemType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemSlotMap extends ItemTypeMap<List<Integer>> {
    public ItemSlotMap() {
    }

    public ItemSlotMap(Map<Item, List<ItemType<?, ?>>> sharedMetaMap) {
        super(sharedMetaMap);
    }
    public void add(ItemStack stack, int slot) {
        ItemType<?, ?> type = getItemType(stack);
        if (putIfAbsent(type, new ArrayList<>(slot)) == null) return;
        List<Integer> value = get(type);
        if(!value.contains(slot)) value.add(slot);
    }
    public void remove(ItemStack stack, int slot){
        get(stack).remove((Integer) slot);
    }
}
