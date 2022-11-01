package com.vicious.viciouscore.common.util.item;

import com.vicious.viciouscore.common.recipe.ingredients.type.ItemTypeKey;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class ItemTypeMap<T> extends HashMap<ItemTypeKey,T> {
    public ItemTypeMap(){}
    protected Object getTypeCast(Object in){
        if(in instanceof ItemStack s){
            return getItemType(s);
        }
        return in;
    }
    protected ItemTypeKey getItemType(ItemStack stack){
        return ItemTypeKey.of(stack);
    }

    @Override
    public T get(Object key) {
        return super.get(getTypeCast(key));
    }
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(getTypeCast(key));
    }
}
