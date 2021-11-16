package com.vicious.viciouscore.common.player;

import com.vicious.viciouscore.common.configuration.StructureComponentConfiguration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ViciousCorePlayerInfo {
    public BlockPos pos1;
    public BlockPos pos2;
    public World world;
    public StructureComponentConfiguration loadedStructure;
    public Runnable confirmableTask;
    public void confirm(){
        confirmableTask.run();
    }
}
