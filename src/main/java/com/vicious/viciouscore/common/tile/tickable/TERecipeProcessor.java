package com.vicious.viciouscore.common.tile.tickable;

import com.vicious.viciouscore.common.data.SyncableTickableData;
import com.vicious.viciouscore.common.recipe.VCRecipe;
import com.vicious.viciouscore.common.recipe.handlers.VCRecipeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class TERecipeProcessor<INGREDIENT,RECIPETYPE extends VCRecipe<INGREDIENT>> extends TETickable{
    protected RECIPETYPE activeRecipe = null;
    protected SyncableTickableData tickData = addSyncable(new SyncableTickableData());

    public TERecipeProcessor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean verifyRecipe(List<INGREDIENT> ingredients, VCRecipeHandler<INGREDIENT,RECIPETYPE> handler){
        if(activeRecipe != null && activeRecipe.validateMatches(ingredients)) return true;
        else{
            activeRecipe = ingredients.size() > 0 ? handler.getRecipe(ingredients) : null;
        }
        if(activeRecipe == null){
            reset();
            return false;
        }
        return true;
    }

    protected void reset(){
        tickData.tickTimeElapsed.set(0);
        tickData.tickTimeForCompletion.set(0);
        setChanged();
    }
}
