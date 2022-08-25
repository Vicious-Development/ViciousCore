package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.recipe.state.RecipeState;
import com.vicious.viciouscore.common.util.item.ItemStackMap;

import java.util.List;

public class SyncableRecipeState<T extends VCRecipe> extends SyncableINBTCompound<RecipeState<T>> {
    public SyncableRecipeState(String key, RecipeState<T> defVal) {
        super(key, defVal);
    }

    public boolean verifyRecipe(List<Object> ingredients){
        return value.verifyRecipe(ingredients);
    }
    public boolean verifyRecipe(ItemStackMap ingredients){
        return value.verifyRecipe(ingredients);
    }

    public T getCurrent(){
        return value.getCurrent();
    }
}
