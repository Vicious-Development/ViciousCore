package com.vicious.viciouscore.common.entity.projectile;

import net.minecraft.world.World;

public class GenericModeledProjectile extends GenericProjectileEntity{
    public GenericModeledProjectile(World world) {
        this(world,0);
    }

    public GenericModeledProjectile(World world, int expirationTicks) {
        super(world, expirationTicks);
    }
}
