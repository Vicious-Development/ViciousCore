package com.vicious.viciouscore.common.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.render.CCModel;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.client.render.RenderEventManager;
import com.vicious.viciouscore.client.render.item.RenderEnergoRifle;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Some guns will need us to render arms differently and override crosshairs.
 * IRenderOverride allows us to do that.
 */
public abstract class ItemGun extends Item implements IRenderOverride {
    public int getSightTime(){
        return 5; //Looks through sights in 5 ticks. (250ms on average).
    }

    public void cancelRenderOverlays(RenderGameOverlayEvent e) {
        //Cancel rendering crosshairs if the player is using sights.
        if(e.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS) && RenderEventManager.LOOKSIGHTSANIMATION.currentTick() == getSightTime()) e.setCanceled(true);
    }

    @Override
    public void registerRenderers() {
        ModelRegistryHelper.registerItemRenderer(this, new RenderEnergoRifle());
    }
}
