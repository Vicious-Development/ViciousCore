package com.vicious.viciouscore.common.recipe.handlers;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.recipe.VCRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedRecipeHandler<INGREDIENT,T extends VCRecipe<INGREDIENT>> extends VCRecipeHandler<INGREDIENT,T> {
    protected final Map<INGREDIENT,List<T>> recipeMap = new HashMap<>();
    public void removeRecipe(List<INGREDIENT> inputs){
        T recipe = getRecipe(inputs);
        if(recipe == null){
            ViciousCore.logger.warn("Failed to remove a recipe: Did not find a recipe containing the inputs: " + inputs);
            return;
        }
        removeRecipe(recipe);
    }
    public void removeRecipe(T recipe){
        super.removeRecipe(recipe);
        for (INGREDIENT rin : recipe.getInputs()) {
            List<T> recList = recipeMap.get(rin);
            recList.remove(recipe);
            if (recList.isEmpty()) recipeMap.remove(rin);
        }
    }

    @Override
    public T getRecipe(List<INGREDIENT> in) {
        for (INGREDIENT input : in) {
            List<T> recipeList = recipeMap.get(input);
            if(recipeList == null) continue;
            for (T recipe : recipeList) {
                if(recipe.validateMatches(in)) return recipe;
            }
        }
        return null;
    }
}
