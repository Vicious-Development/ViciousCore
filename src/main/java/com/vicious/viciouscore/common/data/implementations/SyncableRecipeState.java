package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.recipe.state.RecipeState;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public class SyncableRecipeState<T extends VCRecipe> extends SyncableINBTCompound<RecipeState<T>> {
    public SyncableRecipeState(String key, RecipeState<T> defVal) {
        super(key, defVal);
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.RECIPEPROCESSOR);
    }
}
