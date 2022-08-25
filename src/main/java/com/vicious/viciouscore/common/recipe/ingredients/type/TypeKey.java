package com.vicious.viciouscore.common.recipe.ingredients.type;

import com.vicious.viciouscore.common.recipe.ingredients.stack.IngredientStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class TypeKey<T> {
    private static final Map<Class<?>, Ingredifier> ingredifiers = new HashMap<>();
    static {
        registerIngredifier(ItemStack.class,ItemTypeKey::of,ItemTypeKey::asIngredientStack);
        registerIngredifier(Item.class,ItemTypeKey::of,ItemTypeKey::asIngredientStack);
    }

    public static <T,K extends TypeKey<?>,V extends IngredientStack<?>> void registerIngredifier(Class<T> cls, Function<T,K> rawToKey, Function<T,V> rawToIngredient){
        ingredifiers.put(cls,new Ingredifier<>(rawToKey,rawToIngredient));
    }
    protected final T type;
    public TypeKey(T type) {
        this.type = type;
    }


    public static TypeKey<?> of(Object object) {
        Ingredifier i = ingredifiers.get(object.getClass());
        if(i == null) return new TypeKey<>(object);
        else return (TypeKey<?>) i.keyBuilder.apply(object);
    }

    public static IngredientStack<?> asIngredientStack(Object ingredient) {
        Ingredifier i = ingredifiers.get(ingredient.getClass());
        if(i == null) return IngredientStack.EMPTY;
        else return (IngredientStack<?>) i.ingredientBuilder.apply(ingredient);
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

    public record Ingredifier<T,K extends TypeKey<?>, V extends IngredientStack<?>>(
            Function<T,K> keyBuilder,
            Function<T,V> ingredientBuilder
    ){}
}
