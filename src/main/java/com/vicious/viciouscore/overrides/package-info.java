package com.vicious.viciouscore.overrides;

/**
 * This package features examples of custom mod Overrides. This is the only content ViciousCore provides.
 * Why do we provide this content at all? Well, our override mechanism is designed to REQUIRE VCore as a dependency.
 * For this reason it doesn't make much sense for us to make separate mods specifically for patches.
 * If you want your patch to be included in VCore feel free to submit a pull request.
 * ViciousCore by default patches out:
 * TechReborn's Centrifuge recipe bug.
 *
 * How do I make an Override?
 * 1. Create an override class that extends the original. Implement IFieldCloner
 * 2. Create a constructor that takes in an Object (Calling it "original" in this case) as its parameter.
 * 3. If the superclass requires a call to super, do so. The fields you put in super won't matter so don't be too concerned with them.
 * 4. call clone(original) Your constructor is done.
 * The result should look similar to this:
 * public class OverrideCentrifugeRecipeHandler extends RecipeHandler implements IFieldCloner {
 *     public OverrideCentrifugeRecipeHandler(Object in) {
 *         super("");
 *         clone(in);
 *     }
 * }
 */