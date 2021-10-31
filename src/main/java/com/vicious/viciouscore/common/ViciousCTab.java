package com.vicious.viciouscore.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ViciousCTab extends CreativeTabs {
    private Item icon;
    public ViciousCTab(String label, Item icon) {
        super(label);
        this.icon = icon;
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(icon);
    }
}