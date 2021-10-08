package com.vicious.viciouscore.common.entity.projectile;

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

public class GenericProjectileEntity extends Entity {
    protected static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(GenericProjectileEntity.class, DataSerializers.VARINT);
    private int expiry;

    public int ticksExisted = 0;
    //The distance from this entity's hitbox where a collision detection will occur.
    private float hitRange = 0F;

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
    protected Entity getEntityCollision() {
        Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(this.motionX, this.motionY, this.motionZ).grow(hitRange, hitRange, hitRange));
        double d0 = 0.0D;

        for (Entity possibleCollision : entities) {
            if (possibleCollision.canBeCollidedWith() && this.canBeCollidedWith()) {
                AxisAlignedBB axisalignedbb1 = possibleCollision.getEntityBoundingBox().grow(hitRange, hitRange, hitRange);
                RayTraceResult traceResult = axisalignedbb1.calculateIntercept(vec31, vec3);
                //Null means a collision somehow didn't occur (usually this means the hitrange is too small).
                if (traceResult != null) {
                    double distance = vec31.distanceTo(traceResult.hitVec);
                    if (distance < d0) {
                        return possibleCollision;
                    }
                }
            }
        }
        return null;
    }
    //Returns only player collisions.
    protected EntityPlayer getPlayerEntityCollision(){
        Entity entity = getEntityCollision();
        return entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }
    //Returns only collisions that aren't the same type as this entity.
    protected Entity getNonSameEntityCollision(){
        Entity entity = getEntityCollision();
        return entity instanceof GenericProjectileEntity ? null : entity;
    }
    //Returns only collisions that are the same type as this entity.
    protected Entity getSameEntityCollision(){
        Entity entity = getEntityCollision();
        return entity instanceof GenericProjectileEntity ? entity : null;
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
