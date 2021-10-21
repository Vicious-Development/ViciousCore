package com.vicious.viciouscore.common.entity.living;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GenericHumanoidEntity extends GenericEntityMob {
    public GenericHumanoidEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(999, new EntityAIWatchClosest(this, EntityPlayer.class, 16F));
        super.initEntityAI();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //Entity target = getTarget();
        //if(target != null) faceEntity(target,180,180);

    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }
}
