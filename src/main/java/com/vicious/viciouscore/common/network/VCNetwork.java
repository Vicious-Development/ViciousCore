package com.vicious.viciouscore.common.network;

import com.vicious.viciouscore.common.network.packets.CMessageButtonPressReceived;
import com.vicious.viciouscore.common.resource.VCResources;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class VCNetwork {
    public static VCNetwork instance;
    public static VCNetwork getInstance(){
        if(instance == null) instance = new VCNetwork();
        return instance;
    }
    private static final String VERSION = "1";
    protected final SimpleChannel channel = NetworkRegistry.newSimpleChannel(VCResources.NETWORK,VCNetwork::getProtocolVersion,VERSION::equals,VERSION::equals);

    public VCNetwork(){

    }

    public static String getProtocolVersion(){
        return VERSION;
    }

    public <T> void register(Class<T> pktCls, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf,T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler) {
        VCPacket.register(CMessageButtonPressReceived.class,CMessageButtonPressReceived::new);
    }

    public void sendToServer(VCPacket pk) {
        channel.sendToServer(pk);
    }

    public void reply(NetworkEvent.Context ctx, VCPacket packet) {
        channel.reply(packet,ctx);
    }
}
