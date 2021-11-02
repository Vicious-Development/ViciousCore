package com.vicious.viciouscore.common.item;

import codechicken.lib.model.ModelRegistryHelper;
import com.vicious.viciouscore.client.render.item.RenderEnergoRifle;
import com.vicious.viciouscore.client.render.item.configuration.OverrideConfigurations;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.client.event.RenderSpecificHandEvent;

public class ItemEnergoRifle extends ItemGun {
    private OverrideConfigurations renderconfig;
    public ItemEnergoRifle(String name){
        super(name);
    }
    @Override
    public void renderClient(RenderSpecificHandEvent e) {
        //e.getHand()
    }
    @Override
    public void registerRenderers() {
        ModelRegistryHelper.registerItemRenderer(this, new RenderEnergoRifle());
        renderconfig = OverrideConfigurations.create(this);
        renderconfig.addEntityModelOverrider(ModelBiped.class);
    }
}
