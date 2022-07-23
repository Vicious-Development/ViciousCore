package com.vicious.viciouscore.common.recipe;


import java.util.List;

public abstract class VCRecipe<INGREDIENT> {
    protected int tickRequirement;
    public abstract boolean validateMatches(List<INGREDIENT> in);
    public Integer getTickrequirement() {
        return tickRequirement;
    }

    public abstract List<INGREDIENT> getInputs();
    public abstract List<INGREDIENT> getOutputs();
}
