package com.vicious.viciouscore.client.registries;

import codechicken.lib.model.ModelRegistryHelper;
import com.vicious.viciouscore.client.render.item.RenderEnergoRifle;
import com.vicious.viciouscore.client.render.living.RenderGenericHumanoidEntity;
import com.vicious.viciouscore.client.render.projectile.RenderOrbProjectile;
import com.vicious.viciouscore.common.entity.living.GenericHumanoidEntity;
import com.vicious.viciouscore.common.entity.projectile.OrbProjectile;
import com.vicious.viciouscore.common.item.ItemEnergoRifle;
import com.vicious.viciouscore.common.item.ItemGun;
import com.vicious.viciouscore.common.registries.Registrator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderRegistry extends Registrator {
    public static void register(){
        //Entities
        RenderingRegistry.registerEntityRenderingHandler(OrbProjectile.class, RenderOrbProjectile::new);
        RenderingRegistry.registerEntityRenderingHandler(GenericHumanoidEntity.class, RenderGenericHumanoidEntity::new);

        //Items
        ModelRegistryHelper.registerItemRenderer(new ItemEnergoRifle(), new RenderEnergoRifle());
    }
}
