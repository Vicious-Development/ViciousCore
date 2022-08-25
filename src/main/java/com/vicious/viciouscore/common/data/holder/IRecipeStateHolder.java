package com.vicious.viciouscore.common.data.holder;

import com.vicious.viciouscore.common.data.implementations.SyncableRecipeState;
import com.vicious.viciouscore.common.recipe.VCRecipe;

public interface IRecipeStateHolder<T extends VCRecipe> extends ISyncableCompoundHolder{
    SyncableRecipeState<T> getRecipeState();
}
