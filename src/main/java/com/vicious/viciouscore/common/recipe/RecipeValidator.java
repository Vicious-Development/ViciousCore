package com.vicious.viciouscore.common.recipe;

import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeValidator {
    public static boolean validate(List<ItemStack> stacks, List<ItemStack> inputs){
        for(ItemStack i : stacks){
            boolean itemvalid = false;
            for(ItemStack ex : inputs){
                if(itemEqualsAndSufficient(i, ex)) {
                    itemvalid = true;
                    break;
                }
            }
            if(!itemvalid){
                return false;
            }
        }
        return true;
    }
    public static boolean itemEqualsAndSufficient(ItemStack in, ItemStack expected){
        boolean ret = in.isItemEqual(expected) && in.getCount() >= expected.getCount();
        if(!ret) return false;
        if(in.getTagCompound() != null) ret = in.getTagCompound().equals(expected.getTagCompound());
        return ret;
    }
}
