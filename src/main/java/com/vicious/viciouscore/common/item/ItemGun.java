package com.vicious.viciouscore.common.item;

import codechicken.lib.model.ModelRegistryHelper;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.client.render.entity.model.OverrideModelBiped;
import com.vicious.viciouscore.client.render.entity.model.RenderOverrideHandler;
import com.vicious.viciouscore.client.render.item.RenderEnergoRifle;
import com.vicious.viciouscore.client.render.item.configuration.EntityModelOverride;
import com.vicious.viciouscore.client.render.item.configuration.OverrideConfigurations;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
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

    @Override
    public void renderEntity(Render<?> renderer, EntityLivingBase e) {
        //Changes how the entity renders while holding the item.
        if(renderer instanceof RenderBiped) {
            RenderLiving<?> entityRenderer = (RenderLiving<?>) renderer;
            OverrideModelBiped model = RenderOverrideHandler.overrideModelBiped((RenderBiped<?>) entityRenderer);
            EntityModelOverride<ModelBiped> configurations = OverrideConfigurations.getConfiguration(this).getEntityModelConfig(model);
            model.ignoreHandSides.add(EnumHandSide.RIGHT);
            model.transforms.offer(()->{
                model.applicate(configurations);
            });
        }
    }

    @Override
    public void registerRenderers() {
        ModelRegistryHelper.registerItemRenderer(this, new RenderEnergoRifle());
        OverrideConfigurations.create(this);
    }
}
