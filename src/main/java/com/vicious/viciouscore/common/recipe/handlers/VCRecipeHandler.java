package com.vicious.viciouscore.common.recipe.handlers;


import com.vicious.viciouscore.common.recipe.VCRecipe;

import java.util.ArrayList;
import java.util.List;

public class VCRecipeHandler<INGREDIENT,T extends VCRecipe<INGREDIENT>> {
    protected List<T> recipes = new ArrayList<>();
    public T getRecipe(List<INGREDIENT> in){
        for (T recipe : recipes) {
            if(recipe.validateMatches(in)) return recipe;
        }
        return null;
    }
    public void removeRecipe(T recipe){
        recipes.remove(recipe);
    }
    public void addRecipe(T recipe){
        recipes.add(recipe);
    }
}
