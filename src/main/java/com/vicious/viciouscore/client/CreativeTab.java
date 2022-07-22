package com.vicious.viciouscore.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab extends ItemGroup {
    public Item icon;
    public CreativeTab(String name){
        super(name);
    }

    @Override
    public ItemStack createIcon() {
        return icon.getDefaultInstance();
    }
    /*@Override
    public void fill(NonNullList<ItemStack> items){
        super.fill(items);
    }*/
}
