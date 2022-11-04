package com.vicious.viciouscore.common.recipe;


import com.vicious.viciouscore.common.recipe.ingredients.IRecipeOutputAcceptor;
import com.vicious.viciouscore.common.recipe.ingredients.IngredientStackMap;
import com.vicious.viciouscore.common.recipe.ingredients.stack.IngredientStack;
import com.vicious.viciouscore.common.recipe.ingredients.type.ItemTypeKey;
import com.vicious.viciouscore.common.recipe.ingredients.type.TypeKey;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class VCRecipe {
    protected IngredientStackMap inputs;
    protected IngredientStackMap outputs;

    public int ID;

    public boolean containsThis(List<Object> in) {
        return containsThis(new IngredientStackMap().addAll(in));
    }
    public boolean containsThis(IngredientStackMap in){
        return in.hasAll(inputs);
    }

    /**
     * Only for item recipes. This is here for convenience handling of item only recipes.
     * Using this for any recipe with non-item ingredients will always return false.
     */
    public boolean containsThis(ItemStackMap in){
        for (TypeKey<?> typeKey : inputs.keySet()) {
            if(typeKey instanceof ItemTypeKey item){
                ItemStack stack = in.get(item);
                if(stack == null || stack.getCount() < inputs.get(item).getCount()) return false;
            } else return false;
        }
        return true;
    }

    public boolean consumes(Object... objs){
        IngredientStackMap map = new IngredientStackMap();
        for (Object obj : objs) {
            map.add(obj);
        }
        return inputs.hasAll(map);
    }
    public boolean produces(Object... objs){
        IngredientStackMap map = new IngredientStackMap();
        for (Object obj : objs) {
            map.add(obj);
        }
        return outputs.hasAll(map);
    }

    public VCRecipe(List<Object> inputs, List<Object> outputs) {
        this.inputs = new IngredientStackMap().addAll(inputs);
        this.outputs = new IngredientStackMap().addAll(outputs);
    }

    public IngredientStackMap getInputs(){
        return inputs;
    }
    public IngredientStackMap getOutputs(){
        return outputs;
    }
    public void addToOutputs(IRecipeOutputAcceptor<Object> acceptor){
        List<Object> outputs = new ArrayList<>();
        for (IngredientStack<?> stack : this.outputs.getStacks()) {
            outputs.add(stack.deingredify());
        }
        acceptor.acceptOutputs(outputs);
    }

}
