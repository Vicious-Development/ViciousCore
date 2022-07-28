package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketSyncData<T extends SyncTarget> extends VCPacket {
    private final CompoundTag nbt;
    private final T target;
    public PacketSyncData(T target, CompoundTag tag){
        this.target =target;
        this.nbt = tag;
    }
    public PacketSyncData(T target, FriendlyByteBuf buf){
        this.target=target;
        nbt = buf.readNbt();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf){
        target.toBytes(buf);
        buf.writeNbt(this.nbt);
    }

    public T getTarget() {
        return target;
    }

    public CompoundTag getNBT() {
        return nbt;
    }
}
