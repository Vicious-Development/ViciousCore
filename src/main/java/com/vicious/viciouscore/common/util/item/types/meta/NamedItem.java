package com.vicious.viciouscore.common.util.item.types.meta;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Differentiates items with custom display names.
 */
public class NamedItem extends ForgeItem {
    public NamedItem(Item type) {
        super(type);
    }

    public NamedItem(Item type, CompoundTag tag) {
        super(type, tag);
    }

    public NamedItem(ItemStack stack) {
        super(stack.getItem());
    }
}
