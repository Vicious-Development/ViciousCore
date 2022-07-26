package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.phantom.WorldPosition;
import com.vicious.viciouscore.common.util.ServerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

public class SidedWorldPos extends WorldPosition implements INBTSerializable<CompoundTag> {
    private Direction side;
    public SidedWorldPos(Level level, BlockPos position, Direction side) {
        super(level, position);
        this.side = side;
    }

    public SidedWorldPos(Level level, int x, int y, int z, Direction side) {
        super(level, x, y, z);
        this.side=side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SidedWorldPos that = (SidedWorldPos) o;
        return side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), side);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("s",side.ordinal());
        tag.putInt("x",position.getX());
        tag.putInt("y",position.getY());
        tag.putInt("z",position.getZ());
        tag.putString("w", ServerHelper.getLevelName((ServerLevel) level));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        side = Direction.values()[nbt.getInt("s")];
        position = new BlockPos(nbt.getInt("x"),nbt.getInt("y"),nbt.getInt("z"));
        level = ServerHelper.getLevelByName(nbt.getString("w"));
    }
}
