package com.vicious.viciouscore.common.util.item;

import net.minecraft.world.item.ItemStack;

public class ItemHelper {
    public static boolean doItemsMatch(ItemStack s1, ItemStack s2){
        if(!(s1.getItem().equals(s2.getItem()))) return false;
        else{
            if(s1.hasTag()){
                if(s2.hasTag()){
                    return s1.getTag().equals(s2.getTag());
                }
                else return false;
            }
            return !s2.hasTag();
        }
    }

    public static int getStackSpaceRemaining(ItemStack slotStack) {
        return slotStack.getMaxStackSize()-slotStack.getCount();
    }
}
