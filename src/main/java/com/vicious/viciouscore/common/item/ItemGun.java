package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.client.render.IRenderOverride;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Some guns will need us to render arms differently and override crosshairs.
 * IRenderOverride allows us to do that.
 */
public abstract class ItemGun extends ViciousItem implements IRenderOverride {
    public ItemGun(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public int getSightTime(){
        return 5; //Looks through sights in 5 ticks. (250ms on average).
    }

    public void cancelRenderOverlays(RenderGameOverlayEvent e) {
        //Cancel rendering crosshairs if the player is using sights.
        //if(e.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS) && RenderEventManager.LOOKSIGHTSANIMATION.currentTick() >= getSightTime()) e.setCanceled(true);
    }
}
