package com.vicious.viciouscore.common.recipe.state;

import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.recipe.handlers.VCRecipeHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class RecipeState<RECIPETYPE extends VCRecipe> implements INBTSerializable<CompoundTag> {
    protected int currentRecipe;
    protected VCRecipeHandler<RECIPETYPE> handler;
    public RecipeState(VCRecipeHandler<RECIPETYPE> handler){
        this.handler=handler;
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag ret = new CompoundTag();
        ret.putInt("current",currentRecipe);
        return ret;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        currentRecipe = nbt.getInt("current");
    }

    public boolean verifyRecipe(List<Object> ingredients){
        RECIPETYPE current = getCurrent();
        if (current != null) {
            return current.containsThis(ingredients);
        }
        else{
            current = ingredients.size() > 0 ? handler.getRecipe(ingredients) : null;
            currentRecipe = current == null ? -1 : current.ID;
        }
        return currentRecipe != -1;
    }
    public boolean verifyRecipe(ItemStackMap ingredients){
        RECIPETYPE current = getCurrent();
        if (current != null) {
            return current.containsThis(ingredients);
        }
        else{
            current = ingredients.size() > 0 ? handler.getRecipe(ingredients) : null;
            currentRecipe= current == null ? -1 : current.ID;
        }
        return currentRecipe != -1;
    }

    public RECIPETYPE getCurrent(){
        if(currentRecipe == -1) return null;
        return handler.getRecipe(currentRecipe);
    }
}
