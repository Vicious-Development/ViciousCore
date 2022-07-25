package com.vicious.viciouscore.common.data.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class TickState implements INBTSerializable<CompoundTag> {
    private int progress = 0;
    private int completion;
    public TickState(int completion){
        this.completion=completion;
    }
    public void tick(){
        progress++;
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag ret = new CompoundTag();
        ret.putInt("p",progress);
        ret.putInt("c",completion);
        return ret;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        progress=nbt.getInt("p");
        completion=nbt.getInt("c");
    }
}
