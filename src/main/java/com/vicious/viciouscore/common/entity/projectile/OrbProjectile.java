package com.vicious.viciouscore.common.entity.projectile;

import net.minecraft.world.World;

public class OrbProjectile extends GenericModeledProjectile{
    public OrbProjectile(World world) {
        super(world);
    }

    public OrbProjectile(World world, int expirationTicks) {
        super(world, expirationTicks);
    }
}
