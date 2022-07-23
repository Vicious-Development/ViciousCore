package com.vicious.viciouscore.common.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemRecipe extends VCRecipe<ItemStack>{
    protected ArrayList<ItemStack> inputItems = new ArrayList<>(), outputItems = new ArrayList<>();
    protected boolean isStrict = false;

    public boolean validateMatches(List<ItemStack> in) {
        int matchCount = 0;
        ArrayList<ItemStack> inputs = (ArrayList<ItemStack>) this.inputItems.clone();
        for (int i = 0; i < in.size(); i++) {
            ItemStack item = in.get(i);
            ItemStack recipeInput = null;
            for (int j = 0; j < inputs.size(); j++) {
                recipeInput = inputs.get(j);
                if (item.getItem().equals(recipeInput.getItem())) {
                    if (item.getCount() >= recipeInput.getCount()) {
                        if (isStrict) {
                            if (item.getTag().equals(recipeInput.getTag())) {
                                matchCount++;
                            } else continue;
                        } else {
                            matchCount++;
                        }
                    } else continue;
                } else continue;
                break;
            }
            if (recipeInput != null) inputs.remove(recipeInput);
        }
        return matchCount == this.inputItems.size();
    }

    //Clone list, return.
    public ArrayList<ItemStack> getOutputs() {
        ArrayList<ItemStack> ret = new ArrayList();
        for (ItemStack i : outputItems) {
            ret.add(i.copy());
        }
        return ret;
    }

    public ArrayList<ItemStack> getInputs() {
        ArrayList<ItemStack> ret = new ArrayList();
        for (ItemStack i : inputItems) {
            ret.add(i.copy());
        }
        return ret;
    }
    //Returns an uncloned list.
    public ArrayList<ItemStack> getInputsUnsafe() {
        return inputItems;
    }
}
