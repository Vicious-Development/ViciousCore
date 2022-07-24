package com.vicious.viciouscore.common.util.item.types.meta;


import com.vicious.viciouscore.common.util.item.types.ItemType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ForgeItem extends ItemType<Item, CompoundTag> {
    public ForgeItem(Item type) {
        super(type);
    }

    public ForgeItem(Item type, CompoundTag tag) {
        super(type, tag);
    }
    public ForgeItem(ItemStack stack){
        this(stack.getItem(),stack.getTag());
    }
}
