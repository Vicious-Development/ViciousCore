package com.vicious.viciouscore.overrides;

import com.vicious.viciouscore.common.recipe.RecipeValidator;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.item.ItemStack;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OverrideTileFusionControlComputer extends TileFusionControlComputer {
    public OverrideTileFusionControlComputer(Object og){
        IFieldCloner.clone(this,og);
        lastTick = -1;
    }
    public OverrideTileFusionControlComputer() {
        super();
        lastTick = -1;
    }

    int topStackSlot = 0;
    int bottomStackSlot = 1;
    int outputStackSlot = 2;
    FusionReactorRecipe currentRecipe = null;
    boolean hasStartedCrafting = false;
    long lastTick;
    private static final Method superDuperUpdate = Reflection.getMethod(TilePowerAcceptor.class, "update",new Class[]{});
    /**
     * Resets crafter progress and recipe
     */
    private void resetCrafter() {
        currentRecipe = null;
        crafingTickTime = 0;
        finalTickTime = 0;
        neededPower = 0;
        hasStartedCrafting = false;
    }

    /**
     * Tries to set current recipe based in inputs in reactor
     */
    private void updateCurrentRecipe() {
        for (final FusionReactorRecipe reactorRecipe : FusionReactorRecipeHelper.reactorRecipes) {
            if (validateReactorRecipe(reactorRecipe)) {
                currentRecipe = reactorRecipe;
                crafingTickTime = 0;
                finalTickTime = currentRecipe.getTickTime();
                neededPower = (int) currentRecipe.getStartEU();
                hasStartedCrafting = false;
                break;
            }
        }
    }

    /**
     * Validates that reactor can execute recipe provided, e.g. has all inputs and can fit output
     *
     * @param recipe FusionReactorRecipe Recipe to validate
     * @return boolean True if reactor can execute recipe provided
     */
    private boolean validateReactorRecipe(FusionReactorRecipe recipe) {
        boolean validRecipe = validateReactorRecipeInputs(recipe, inventory.func_70301_a(topStackSlot), inventory.func_70301_a(bottomStackSlot)) || validateReactorRecipeInputs(recipe, inventory.func_70301_a(bottomStackSlot), inventory.func_70301_a(topStackSlot));
        return validRecipe && getSize() >= recipe.getMinSize();
    }

    private boolean validateReactorRecipeInputs(FusionReactorRecipe recipe, ItemStack slot1, ItemStack slot2) {
        List<ItemStack> in = new ArrayList<>();
        in.add(slot1);
        in.add(slot2);
        List<ItemStack> expected = new ArrayList<>();
        expected.add(recipe.getBottomInput());
        expected.add(recipe.getTopInput());
        if(RecipeValidator.validate(in, expected)){
            return canFitStack(recipe.getOutput(), outputStackSlot, true);
        }
        return false;
    }

    // TilePowerAcceptor
    @Override
    public void update() {
        /*Call super.super.update()
        //Why doesn't java just make this a thing >:/
        Reflection.invokeMethod(this,superDuperUpdate,new Object[]{});
        Disabled for the time being since even with my reflection magic, doing this isn't possible.*/
        if (world.isRemote) {
            return;
        }
        if(lastTick == world.getTotalWorldTime()){
            //Prevent tick accerators, blame obstinate for this.
            return;
        }
        lastTick = world.getTotalWorldTime();

        // Force check every second
        if (world.getTotalWorldTime() % 20 == 0) {
            checkCoils();
            inventory.hasChanged = true;
        }

        if (coilCount == 0) {
            resetCrafter();
            return;
        }

        if (currentRecipe == null && inventory.hasChanged) {
            updateCurrentRecipe();
        }

        if (currentRecipe != null) {
            if (!hasStartedCrafting && inventory.hasChanged && !validateReactorRecipe(currentRecipe)) {
                resetCrafter();
                return;
            }

            if (!hasStartedCrafting) {
                // Ignition!
                if (canUseEnergy(currentRecipe.getStartEU())) {
                    useEnergy(currentRecipe.getStartEU());
                    hasStartedCrafting = true;
                    decrStackSize(currentRecipe.getTopInput());
                    if (!currentRecipe.getBottomInput().isEmpty()) {
                        decrStackSize(currentRecipe.getBottomInput());
                    }
                }
            }
            if (hasStartedCrafting && crafingTickTime < finalTickTime) {
                crafingTickTime++;
                // Power gen
                if (currentRecipe.getEuTick() > 0) {
                    // Waste power if it has no where to go
                    addEnergy(currentRecipe.getEuTick() * getPowerMultiplier());
                    powerChange = currentRecipe.getEuTick() * getPowerMultiplier();
                } else { // Power user
                    if (canUseEnergy(currentRecipe.getEuTick() * -1)) {
                        setEnergy(getEnergy() - currentRecipe.getEuTick() * -1);
                    }
                }
            } else if (crafingTickTime >= finalTickTime) {
                if (canFitStack(currentRecipe.getOutput(), outputStackSlot, true)) {
                    if (func_70301_a(outputStackSlot).isEmpty()) {
                        func_70299_a(outputStackSlot, currentRecipe.getOutput().copy());
                    } else {
                        func_70298_a(outputStackSlot, -currentRecipe.getOutput().getCount());
                    }
                    if (validateReactorRecipe(this.currentRecipe)) {
                        crafingTickTime = 0;
                        decrStackSize(currentRecipe.getTopInput());
                        if (!currentRecipe.getBottomInput().isEmpty()) {
                            decrStackSize(currentRecipe.getBottomInput());
                        }
                    } else {
                        resetCrafter();
                    }
                }
            }
            markDirty();
        }

        if (inventory.hasChanged) {
            inventory.hasChanged = false;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        checkCoils();
        lastTick = -1;
    }

    public void decrStackSize(ItemStack stack){
        if(func_70301_a(topStackSlot).isItemEqual(stack)) inventory.func_70301_a(topStackSlot).shrink(stack.getCount());
        if(func_70301_a(bottomStackSlot).isItemEqual(stack)) inventory.func_70301_a(bottomStackSlot).shrink(stack.getCount());
    }
}