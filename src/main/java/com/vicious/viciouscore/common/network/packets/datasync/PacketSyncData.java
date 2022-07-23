package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketSyncData extends VCPacket {
    private final CompoundTag nbt;
    private final int targetID;
    public PacketSyncData(int windowId, CompoundTag tag){
        this.targetID =windowId;
        this.nbt = tag;
    }
    public PacketSyncData(FriendlyByteBuf buf){
        targetID = buf.readUnsignedByte();
        nbt = buf.readNbt();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(this.targetID);
        buf.writeNbt(this.nbt);
    }

    public int getTargetID() {
        return targetID;
    }

    public CompoundTag getNBT() {
        return nbt;
    }
}
