package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketSyncData extends VCPacket {
    private final CompoundTag nbt;
    private final int instanceId;
    public PacketSyncData(int windowId, CompoundTag tag){
        this.instanceId=windowId;
        this.nbt = tag;
    }
    public PacketSyncData(FriendlyByteBuf buf){
        instanceId = buf.readUnsignedByte();
        nbt = buf.readNbt();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(this.instanceId);
        buf.writeNbt(this.nbt);
    }

    public int getInstanceId() {
        return instanceId;
    }

    public CompoundTag getNBT() {
        return nbt;
    }
}
