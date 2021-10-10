package com.vicious.viciouscore.common.entity.projectile;

import com.vicious.viciouscore.common.entity.GenericEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GenericProjectileEntity extends GenericEntity {
    protected static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(GenericProjectileEntity.class, DataSerializers.VARINT);
    private int expiry;

    public int ticksExisted = 0;
    //The distance from this entity's hitbox where a collision detection will occur.

    public GenericProjectileEntity(World world) {
        this(world,0);
    }
    /**
     * Creates a projectile entity with a set expiry time in ticks. Once the entity has existed for the expiry, it will be killed and destroyed.
     * @param world
     * @param expirationTicks
     */
    public GenericProjectileEntity(World world, int expirationTicks) {
        super(world);
        this.expiry = expirationTicks;
    }



    /**
     * Override this to execute code when expiration occurs.
     */
    protected void onExpire(){
        //TODO:enable
        /*setDead();*/
    }
    @Override
    protected void entityInit() {
        dataManager.register(TICKSEXISTED, ticksExisted);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ticksExisted++;
        if (ticksExisted >= expiry) onExpire();
    }

    @Override
    public boolean canBeCollidedWith() {
        return ticksExisted >= invincibilityTime();
    }

    private int invincibilityTime() {
        //All projectile entities have an invincibility time where they won't collide with other entities.
        return 5;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        ticksExisted = compound.getInteger("existed");
        if (!world.isRemote) {
            dataManager.set(TICKSEXISTED, ticksExisted);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("existed", ticksExisted);
    }
}
