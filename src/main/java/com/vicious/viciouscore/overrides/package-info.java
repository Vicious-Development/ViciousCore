package com.vicious.viciouscore.overrides;

/**
 * This package features examples of custom mod Overrides. This is the only content ViciousCore provides.
 * Why do we provide this content at all? Well, our override mechanism is designed to REQUIRE VCore as a dependency.
 * For this reason it doesn't make much sense for us to make separate mods specifically for our patches.
 * If you want your patch to be included in VCore feel free to submit a pull request.
 *
 * How do I make an Override?
 * 1. Create an override class that extends the original. Implement IFieldCloner
     * NOTE: implementing IFieldCloner may mess with your IDE and ask you to implement all interfaces implemented by the super class.
     * If this occurs, do not implement, call IFieldCloner.clone(this,og) instead.
 * 2. Create a constructor that takes in an Object (Calling it "original" in this case) as its parameter.
 * 3. If the superclass requires a call to super, do so. The fields you put in super won't matter so don't be too concerned with them.
 * 4. call clone(original) Your constructor is done.
 * The result should look similar to this:
 * public class OverrideCentrifugeRecipeHandler extends RecipeHandler implements IFieldCloner {
 *     public OverrideCentrifugeRecipeHandler(Object in) {
 *         super("importanttitle");
 *         clone(in);
 *     }
 * }
 *
 * After that's done, register your Override using one of the Overrider.register...(route,overrider) methods. In the case above I used Overrider.registerInitInjector() since Techreborn initializes the field I accessed on preinit.
 * The FieldRetrievalRoute is created by providing the STARTING CLASS (where the static field is) and then the fields to get to the ending class in order! (final field should be the field you want to override.
 */