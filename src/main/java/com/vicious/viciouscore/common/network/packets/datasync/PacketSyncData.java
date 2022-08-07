package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketSyncData<T extends SyncTarget> extends VCPacket {
    protected CompoundTag nbt;
    protected T target;
    public PacketSyncData(T target, CompoundTag tag){
        this.target = target;
        this.nbt = tag;
    }
    public PacketSyncData(T target, FriendlyByteBuf buf){
        this.nbt=buf.readAnySizeNbt();
        this.target=target;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(this.nbt);
    }

    public T getTarget() {
        return target;
    }

    public CompoundTag getNBT() {
        return nbt;
    }
}
