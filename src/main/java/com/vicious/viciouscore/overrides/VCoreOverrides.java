package com.vicious.viciouscore.overrides;

import com.vicious.viciouscore.common.override.Overrider;
import com.vicious.viciouscore.common.util.reflect.FieldRetrievalRoute;
import net.minecraftforge.fml.common.Loader;

public class VCoreOverrides {
    public static void init(){
        if(Loader.isModLoaded("techreborn")){
            Overrider.registerInitInjector(
                    new FieldRetrievalRoute("techreborn.api.recipe.Recipes", "centrifuge"),
                    OverrideCentrifugeRecipeHandler::new);
        }
    }
}
