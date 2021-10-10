package com.vicious.viciouscore.common.entity.living;

import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GenericHumanoidEntity extends GenericEntityMob {
    public GenericHumanoidEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

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
