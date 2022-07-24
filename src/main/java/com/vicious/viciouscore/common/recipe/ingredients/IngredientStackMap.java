package com.vicious.viciouscore.common.recipe.ingredients;

import com.vicious.viciouscore.common.recipe.ingredients.stack.IngredientStack;
import com.vicious.viciouscore.common.recipe.ingredients.stack.ItemIngredientStack;
import com.vicious.viciouscore.common.recipe.ingredients.type.TypeKey;
import com.vicious.viciouscore.common.util.item.ItemStackMap;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class IngredientStackMap extends HashMap<TypeKey<?>, IngredientStack<?>> {
    public IngredientStackMap() {}

    /**
     * @param stack the stack to add.
     * @return if the Material was already present.
     */
    public boolean add(Object ingredient){
        TypeKey<?> key = TypeKey.of(ingredient);
        IngredientStack<?> stack = TypeKey.asIngredientStack(ingredient);
        if(putIfAbsent(key,TypeKey.asIngredientStack(ingredient)) != null){
            IngredientStack<?> mappedStack = get(key);
            mappedStack.increment(stack.getCount());
            return true;
        }
        return false;
    }

    /**
     * Reduces a stored item
     */
    public boolean reduceBy(Object ingredient){
        TypeKey<?> key = TypeKey.of(ingredient);
        IngredientStack<?> stack = TypeKey.asIngredientStack(ingredient);
        IngredientStack<?> mappedStack = get(key);
        if(mappedStack == null) return false;
        mappedStack.decrement(stack.getCount());
        if(mappedStack.getCount() == 0) remove(key);
        return mappedStack.getCount() < 0;
    }

    public IngredientStackMap addAll(Collection<Object> ingredients){
        for (Object item : ingredients) {
            add(item);
        }
        return this;
    }
    public List<IngredientStack<?>> getStacks(){
        List<IngredientStack<?>> stacks = new ArrayList<>();
        for (IngredientStack<?> value : values()) {
            if(value.getCount() <= 0) continue;
            if(value instanceof ItemIngredientStack iis){
                stacks.addAll(iis.legalize());
            }
            else stacks.add(value);
        }
        return stacks;
    }

    public IngredientStackMap combine(ItemStackMap stacks) {
        for (ItemStack value : stacks.values()) {
            add(value);
        }
        return this;
    }

    /**
     * Checks if the map contains AT LEAST all the elements in the list.
     */
    public boolean hasAll(List<Object> stacks){
        //Map to unify stacks.
        IngredientStackMap mapped = new IngredientStackMap().addAll(stacks);
        for (TypeKey<?> k : mapped.keySet()) {
            IngredientStack<?> mapStack = get(k);
            if (mapStack == null) return false;
            IngredientStack<?> expected = mapped.get(k);
            if (mapStack.getCount() >= expected.getCount()) mapped.remove(k);
            else mapped.reduceBy(mapStack);
        }
        //Mapped should be empty by the end of this.
        return mapped.size() == 0;
    }

    public boolean hasAll(IngredientStackMap inputs) {
        for (TypeKey<?> typeKey : inputs.keySet()) {
            IngredientStack<?> stack = get(typeKey);
            if(stack == null) return false;
            else if(stack.getCount() < inputs.get(typeKey).getCount()) return false;
        }
        return true;
    }
}
