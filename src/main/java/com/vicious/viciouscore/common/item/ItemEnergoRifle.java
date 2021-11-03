package com.vicious.viciouscore.common.item;

import codechicken.lib.model.ModelRegistryHelper;
import com.vicious.viciouscore.client.configuration.ClientOverrideConfigurations;
import com.vicious.viciouscore.client.configuration.HeldItemOverrideCFG;
import com.vicious.viciouscore.client.render.item.RenderEnergoRifle;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnergoRifle extends ItemGun {
    @SideOnly(Side.CLIENT)
    private HeldItemOverrideCFG renderconfig;

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
        renderconfig = ClientOverrideConfigurations.createWhenHeldOverride(this);
        renderconfig.addEntityModelOverrider(ModelBiped.class);
        renderconfig.addEntityModelOverrider(ModelPlayer.class);
    }
    public HeldItemOverrideCFG getConfiguration(){
        return renderconfig;
    }
}
