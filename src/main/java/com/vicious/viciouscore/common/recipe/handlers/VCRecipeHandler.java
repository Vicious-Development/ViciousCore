package com.vicious.viciouscore.common.recipe.handlers;


import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.util.item.ItemStackMap;

import java.util.ArrayList;
import java.util.List;

public class VCRecipeHandler<T extends VCRecipe> {
    protected List<T> recipes = new ArrayList<>();
    public T getRecipe(List<Object> in){
        for (T recipe : recipes) {
            if(recipe.containsThis(in)) return recipe;
        }
        return null;
    }
    public T getRecipe(ItemStackMap ism){
        for (T recipe : recipes) {
            if(recipe.containsThis(ism)) return recipe;
        }
        return null;
    }
    public T getRecipe(int index){
        return recipes.get(index);
    }
    public void removeRecipe(T recipe){
        recipes.remove(recipe);
    }
    public void addRecipe(T recipe){
        recipe.ID=recipes.size();
        recipes.add(recipe);
    }
}
