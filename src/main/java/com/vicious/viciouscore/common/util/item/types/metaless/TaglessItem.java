package com.vicious.viciouscore.common.util.item.types.metaless;

import com.vicious.viciouscore.common.util.item.types.ItemType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * Uses only the item for the hashcode method.
 */
public class TaglessItem<T> extends ItemType<Item,T> {
    public TaglessItem(Item type){
        super(type);
    }
    public TaglessItem(Item type, T meta){
        super(type,meta);
    }
    public TaglessItem(ItemStack stack){
        super(stack.getItem());
    }

    @Override
    public boolean isType(ItemType<?, ?> type, boolean ignoreMeta) {
        return Objects.equals(type.getType(),getType());
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaglessItem<?> itemType = (TaglessItem<?>) o;
        return Objects.equals(type, itemType.type);
    }
    /**
     * Currently only supports vanilla items.
     */
    public static TaglessItem<?> fromItemStack(ItemStack stack){
        if(stack.hasTag()){
            //Most custom items have a custom display name.
            if(stack.hasCustomHoverName()){
                return new MetalessNamedItem(stack);
            }
            else return new TaglessItem<Object>(stack);
        }
        else return new TaglessItem<Object>(stack);
    }
}
