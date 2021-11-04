package com.vicious.viciouscore.overrides;

import net.minecraft.item.ItemStack;
import reborncore.api.praescriptum.ingredients.input.InputIngredient;
import reborncore.api.praescriptum.ingredients.input.ItemStackInputIngredient;
import reborncore.api.praescriptum.recipes.Recipe;
import reborncore.api.praescriptum.recipes.RecipeHandler;
import reborncore.common.util.ItemUtils;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Overridden variant of the centrifugeRecipeHandler that actually processes correctly for fucks sake.
 */
public class OverrideCentrifugeRecipeHandler extends RecipeHandler implements IFieldCloner {
    public OverrideCentrifugeRecipeHandler(Object in) {
        super("getfucked");
        clone(in);
    }

    public Recipe findAndApply(Collection<ItemStack> itemStacks, boolean simulate) {
        System.out.println("Testing");
        Queue<InputIngredient<?>> ingredients = new ArrayDeque();
        Iterator var4 = itemStacks.iterator();
        while(var4.hasNext()) {
            ItemStack stack = (ItemStack)var4.next();
            if (!ItemUtils.isEmpty(stack)) {
                ingredients.add(ItemStackInputIngredient.of(stack));
            }
        }

        if (ingredients.isEmpty()) {
            return null;
        } else {
            Recipe recipe = (Recipe)this.cachedRecipes.get(ingredients);
            if (recipe == null) {
                return null;
            } else if (ingredients.size() != recipe.getInputIngredients().size()) {
                return null;
            } else {
                Queue<InputIngredient<?>> queueA = new ArrayDeque(recipe.getInputIngredients());
                Iterator var6 = ingredients.iterator();

                while(var6.hasNext()) {
                    InputIngredient<?> entry = (InputIngredient)var6.next();

                    queueA.removeIf((temp) -> temp.matchesStrict(entry.ingredient) && entry.getCount() >= temp.getCount());
                }

                if (!queueA.isEmpty()) {
                    return null;
                } else {
                    if (!simulate) {
                        Queue<InputIngredient<?>> queueB = new ArrayDeque(recipe.getInputIngredients());
                        Iterator var12 = ingredients.iterator();

                        while(var12.hasNext()) {
                            InputIngredient<?> entry = (InputIngredient)var12.next();
                            queueB.removeIf((temp) -> {
                                if (temp.matchesStrict(entry.ingredient) && entry.getCount() >= temp.getCount()) {
                                    entry.shrink(temp.getCount());
                                    return true;
                                } else {
                                    return false;
                                }
                            });
                        }
                    }

                    return recipe;
                }
            }
        }
    }
}
