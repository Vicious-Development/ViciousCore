package com.vicious.viciouscore.common.recipe.ingredients.stack;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemIngredientStack extends IngredientStack<ItemStack> {
    private final boolean ignoreNBT;
    public ItemIngredientStack(ItemStack representative) {
        super(representative, representative.getCount());
        ignoreNBT = false;
    }
    public ItemIngredientStack(ItemStack representative, boolean ignoreNBT) {
        super(representative, representative.getCount());
        this.ignoreNBT = ignoreNBT;
    }

    @Override
    public boolean matches(Object in) {
        if(in instanceof ItemStack other){
            boolean matches = other.getItem().equals(representative.getItem()) && other.getCount() >= representative.getCount();
            if(ignoreNBT || !representative.hasTag()) return matches;
            else{
                return matches && representative.getTag().equals(other.getTag());
            }
        }
        else return false;
    }

    @Override
    public void decrement(int decrementation) {
        super.decrement(decrementation);
        representative.setCount(representative.getCount()-decrementation);
    }

    @Override
    public void increment(int incrementation) {
        super.increment(incrementation);
        representative.setCount(representative.getCount()+incrementation);
    }

    @Override
    public ItemStack deingredify() {
        return representative.copy();
    }

    public Collection<? extends IngredientStack<?>> legalize() {
        List<ItemIngredientStack> legalStacks = new ArrayList<>();
        int maxStackCount = representative.getCount()/representative.getMaxStackSize();
        int modular = representative.getCount()%representative.getMaxStackSize();
        if(maxStackCount > 0){
            for (int i = 0; i < maxStackCount; i++) {
                ItemStack toAdd = new ItemStack(representative.getItem(),representative.getMaxStackSize());
                if(representative.hasTag()) toAdd.setTag(representative.getTag());
                legalStacks.add(new ItemIngredientStack(toAdd));
            }
        }
        if(modular != 0) {
            ItemStack toAdd = new ItemStack(representative.getItem(), modular);
            toAdd.setTag(representative.getTag());
            legalStacks.add(new ItemIngredientStack(toAdd));
        }
        return legalStacks;
    }
}
