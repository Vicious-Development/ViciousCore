package com.vicious.viciouscore.common.recipe.handlers;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.recipe.ingredients.type.TypeKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedRecipeHandler<T extends VCRecipe> extends VCRecipeHandler<T> {
    protected final Map<TypeKey<?>,List<T>> recipeMap = new HashMap<>();
    public void removeRecipe(List<Object> inputs){
        T recipe = getRecipe(inputs);
        if(recipe == null){
            ViciousCore.logger.warn("Failed to remove a recipe: Did not find a recipe containing the inputs: " + inputs);
            return;
        }
        removeRecipe(recipe);
    }
    public void removeRecipe(T recipe){
        super.removeRecipe(recipe);
        for (TypeKey<?> rin : recipe.getInputs().keySet()) {
            List<T> recList = recipeMap.get(rin);
            recList.remove(recipe);
            if (recList.isEmpty()) recipeMap.remove(rin);
        }
    }

    @Override
    public void addRecipe(T recipe) {
        super.addRecipe(recipe);
        for (TypeKey<?> typeKey : recipe.getInputs().keySet()) {
            if(recipeMap.putIfAbsent(typeKey, Lists.newArrayList(recipe)) != null){
                recipeMap.get(typeKey).add(recipe);
            }
        }
    }

    @Override
    public T getRecipe(List<Object> in) {
        for (Object o : in) {
            TypeKey<?> key = TypeKey.of(o);
            List<T> options = recipeMap.get(key);
            for (T option : options) {
                if(option.containsThis(in)){
                    return option;
                }
            }
        }
        return null;
    }

    public List<T> getRecipesOfIngedrient(Object stack){
        List<T> lst = recipeMap.get(TypeKey.of(stack));
        return lst != null ? lst : new ArrayList<>();
    }
}
