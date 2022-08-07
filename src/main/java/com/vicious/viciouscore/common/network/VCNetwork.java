package com.vicious.viciouscore.common.network;

import com.vicious.viciouscore.common.network.packets.datasync.CPacketSyncData;
import com.vicious.viciouscore.common.network.packets.datasync.SPacketSyncData;
import com.vicious.viciouscore.common.network.packets.keybindpress.CPacketButtonPressReceived;
import com.vicious.viciouscore.common.network.packets.keybindpress.SPacketButtonUpdate;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotKeyPressed;
import com.vicious.viciouscore.common.resource.VCResources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class VCNetwork {
    public static VCNetwork instance;
    public static VCNetwork getInstance(){
        if(instance == null){
            instance = new VCNetwork();
            VCPacket.register(CPacketButtonPressReceived.class, CPacketButtonPressReceived::new);
            VCPacket.register(SPacketButtonUpdate.class, SPacketButtonUpdate::new);
            VCPacket.register(CPacketSyncData.Window.class, CPacketSyncData.Window::new);
            VCPacket.register(SPacketSyncData.Window.class, SPacketSyncData.Window::new);
            VCPacket.register(SPacketSlotClicked.class, SPacketSlotClicked::new);
            VCPacket.register(SPacketSlotKeyPressed.class, SPacketSlotKeyPressed::new);
        }
        return instance;
    }
    private static final String VERSION = "1";
    protected final SimpleChannel channel = NetworkRegistry.newSimpleChannel(VCResources.NETWORK,VCNetwork::getProtocolVersion,VERSION::equals,VERSION::equals);

    public VCNetwork(){
    }

    public static String getProtocolVersion(){
        return VERSION;
    }
    public void sendToServer(VCPacket pk) {
        channel.sendToServer(pk);
    }
    public void sendToPlayer(ServerPlayer plr, VCPacket packet){
        channel.send(PacketDistributor.PLAYER.with(()->plr),packet);
    }

    public void reply(NetworkEvent.Context ctx, VCPacket packet) {
        channel.reply(packet,ctx);
    }
}
