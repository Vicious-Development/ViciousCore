package com.vicious.viciouscore.common.recipe.ingredients.type;

import com.vicious.viciouscore.common.recipe.ingredients.stack.IngredientStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.Objects;

public class TypeKey<T> {
    protected final T type;
    public TypeKey(T type) {
        this.type = type;
    }

    public static TypeKey<?> of(Object object) {
        if(object instanceof ItemStack stack){
            return ItemTypeKey.of(stack);
        }
        if(object instanceof Item item){
            return ItemTypeKey.of(item);
        }
        else return new TypeKey<>(object);
    }

    public static IngredientStack<?> asIngredientStack(Object ingredient) {
        if(ingredient instanceof ItemStack stack){
            return ItemTypeKey.asIngredientStack(stack);
        }
        if(ingredient instanceof Item item){
            return ItemTypeKey.asIngredientStack(item);
        }
        else return IngredientStack.EMPTY;
    }

    public T getType(){
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeKey<?> that = (TypeKey<?>) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
