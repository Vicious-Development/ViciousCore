package com.vicious.viciouscore.common.network.packets.datasync;


import com.vicious.viciouscore.common.data.DataEditor;
import com.vicious.viciouscore.common.data.VCDataSyncHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSyncData extends PacketSyncData{
    public SPacketSyncData(int instanceId, CompoundTag tag) {
        super(instanceId, tag);
    }

    public SPacketSyncData(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        VCDataSyncHandler.getInstance().get(getInstanceId()).readFromNBT(getNBT(), DataEditor.of(context.get().getSender()));
    }
}
