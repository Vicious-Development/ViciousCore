package com.vicious.viciouscore.client.network;

import com.drathonix.zeroenergy.common.network.packets.SSyncContainerStateData;
import com.drathonix.zeroenergy.common.network.packets.ZEClientReceptablePacketHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncContainerStateDataHandler extends ZEClientReceptablePacketHandler {
    public static void onPacketReceived(SSyncContainerStateData packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().setPacketHandled(true);
        if(!isClientSide(ctx)) return;
        ctx.get().enqueueWork(() -> ZEClientNetplayHandler.handleStateChange(packet));
    }
}
