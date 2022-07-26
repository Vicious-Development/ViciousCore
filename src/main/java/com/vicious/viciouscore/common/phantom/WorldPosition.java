package com.vicious.viciouscore.common.phantom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class WorldPosition {
    public Level level;
    public BlockPos position;
    public WorldPosition(Level level, BlockPos position) {
        this.level = level;
        this.position = position;
    }
    public WorldPosition(Level level, int x, int y, int z){
        this(level, new BlockPos(x,y,z));
    }

    public WorldPosition inLevel(Level l){
        return new WorldPosition(l,position);
    }
    public WorldPosition add(int x, int y, int z){
        return new WorldPosition(level,x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldPosition that = (WorldPosition) o;
        return Objects.equals(level, that.level) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, position);
    }
}
