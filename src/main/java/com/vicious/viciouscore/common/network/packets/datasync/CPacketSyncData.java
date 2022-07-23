package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.data.DataEditor;
import com.vicious.viciouscore.common.data.VCDataSyncHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSyncData extends PacketSyncData{
    private static final DataEditor.Client editor = new DataEditor.Client();

    public CPacketSyncData(int windowId, CompoundTag tag) {
        super(windowId, tag);
    }

    public CPacketSyncData(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        VCDataSyncHandler.getInstance().get(getInstanceId()).readFromNBT(getNBT(), editor);
    }
}
