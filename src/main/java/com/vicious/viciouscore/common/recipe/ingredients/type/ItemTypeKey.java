package com.vicious.viciouscore.common.recipe.ingredients.type;

import com.vicious.viciouscore.common.recipe.ingredients.stack.ItemIngredientStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ItemTypeKey extends TypeKey<Item> {
    protected CompoundTag tag;
    public ItemTypeKey(Item type) {
        super(type);
    }
    public ItemTypeKey(Item type, CompoundTag tag){
        this(type);
        this.tag=tag;
    }
    public static ItemTypeKey of(ItemStack stack){
        if(stack.hasTag()) return new ItemTypeKey(stack.getItem(),stack.getTag());
        else return new ItemTypeKey(stack.getItem());
    }
    public static ItemTypeKey of(Item item){
        return new ItemTypeKey(item);
    }
    public static ItemIngredientStack asIngredientStack(ItemStack stack){
        return new ItemIngredientStack(stack);
    }
    public static ItemIngredientStack asIngredientStack(Item item){
        return new ItemIngredientStack(new ItemStack(item));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ItemTypeKey that = (ItemTypeKey) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tag);
    }
}
