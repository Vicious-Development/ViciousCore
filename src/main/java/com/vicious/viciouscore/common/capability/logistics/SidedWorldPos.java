package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.phantom.WorldPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SidedWorldPos extends WorldPos {
    public Direction side;
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
        CompoundTag tag = super.serializeNBT();
        tag.putInt("s",side.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        side = Direction.values()[nbt.getInt("s")];
    }
}
