package com.vicious.viciouscore.common.util.item;

import com.vicious.viciouscore.common.util.item.types.ItemType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTypeMap<T> extends HashMap<ItemType<?,?>,T> {
    protected final Map<Item, List<ItemType<?,?>>> metaMap;
    public ItemTypeMap(){
        metaMap=new HashMap<>();
    }
    public ItemTypeMap(Map<Item,List<ItemType<?,?>>> sharedMetaMap){
        metaMap=sharedMetaMap;
    }
    protected Object getTypeCast(Object in){
        if(in instanceof ItemStack s){
            return getItemType(s);
        }
        return in;
    }
    protected ItemType<?,?> getItemType(ItemStack stack){
        List<ItemType<?,?>> stl = metaMap.get(stack.getItem());
        if(stl != null) {
            if (stl.size() == 1) {
                return stl.get(0);
            } else if (stl.size() > 1) {
                for (ItemType<?,?> st : stl) {
                    if (st.isType(ItemType.fromItemStack(stack),false)) {
                        return st;
                    }
                }
            }
        }
        List<ItemType<?,?>> lst = new ArrayList<>();
        ItemType<?,?> st = ItemType.fromItemStack(stack);
        lst.add(st);
        metaMap.put(stack.getItem(),lst);
        return st;
    }

    @Override
    public T get(Object key) {
        return super.get(getTypeCast(key));
    }
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(getTypeCast(key));
    }

    public void set(@NotNull ItemStack stack, T slot) {
        ItemType<?,?> key = getItemType(stack);
        if(!containsKey(key)){
            put(key,slot);
        }
        else replace(key,slot);
    }
}
