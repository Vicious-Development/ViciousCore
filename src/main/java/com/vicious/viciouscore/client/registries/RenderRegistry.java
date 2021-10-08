package com.vicious.viciouscore.client.registries;

import com.vicious.viciouscore.client.rendering.RenderOrbProjectile;
import com.vicious.viciouscore.common.entity.projectile.OrbProjectile;
import com.vicious.viciouscore.common.registries.Registrator;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderRegistry extends Registrator {
    public static void register(){
        RenderingRegistry.registerEntityRenderingHandler(OrbProjectile.class, RenderOrbProjectile::new);
    }
}
