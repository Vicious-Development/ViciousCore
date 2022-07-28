package com.vicious.viciouscore.common.recipe.ingredients;

import java.util.List;

public interface IRecipeOutputAcceptor<T> {
    void acceptOutputs(List<T> outputs);
}
