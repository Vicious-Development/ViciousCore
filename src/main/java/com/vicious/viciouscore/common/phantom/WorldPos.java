package com.vicious.viciouscore.common.phantom;

import com.vicious.viciouscore.common.util.server.ServerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

public class WorldPos implements INBTSerializable<CompoundTag> {
    public Level level;
    public BlockPos position;
    public WorldPos(Level level, BlockPos position) {
        this.level = level;
        this.position = position;
    }
    public WorldPos(Level level, int x, int y, int z){
        this(level, new BlockPos(x,y,z));
    }

    public WorldPos inLevel(Level l){
        return new WorldPos(l,position);
    }
    public WorldPos add(int x, int y, int z){
        return new WorldPos(level,x,y,z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldPos that = (WorldPos) o;
        return Objects.equals(level, that.level) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, position);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x",position.getX());
        tag.putInt("y",position.getY());
        tag.putInt("z",position.getZ());
        tag.putString("w", ServerHelper.getLevelName((ServerLevel) level));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        position = new BlockPos(nbt.getInt("x"),nbt.getInt("y"),nbt.getInt("z"));
        level = ServerHelper.getLevelByName(nbt.getString("w"));
    }
}
