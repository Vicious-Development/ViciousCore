package com.vicious.viciouscore.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class VCPacket {
    private static int id = -1;
    public static int nextId(){
        id++;
        return id;
    }
    public abstract void toBytes(FriendlyByteBuf buf);
    public abstract void handle(Supplier<NetworkEvent.Context> context);
    public static <T extends VCPacket> void register(Class<T> type, Function<FriendlyByteBuf,T> decoderConstructor){
        VCNetwork.getInstance().channel.registerMessage(nextId(),type, VCPacket::toBytes,decoderConstructor, VCPacket::handle);
    }
}
