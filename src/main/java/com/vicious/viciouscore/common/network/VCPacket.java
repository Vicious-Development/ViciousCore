package com.vicious.viciouscore.common.network;

import com.vicious.viciouscore.common.util.SidedExecutor;
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
    public boolean handleOnServer(){
        return false;
    }
    public boolean handleOnClient(){
        return false;
    }

    public abstract void toBytes(FriendlyByteBuf buf);
    public abstract void handle(Supplier<NetworkEvent.Context> context);
    public static <T extends VCPacket> void register(Class<T> type, Function<FriendlyByteBuf,T> decoderConstructor){
        VCNetwork.getInstance().channel.registerMessage(nextId(),type, (pk,buf)->{
            try{
                pk.toBytes(buf);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        },decoderConstructor,(pk,ctx)->{
            try{
                if(pk.handleOnClient()){
                    SidedExecutor.clientOnly(()->pk.handle(ctx));
                }
                else if(pk.handleOnServer()){
                    SidedExecutor.serverOnly(()->pk.handle(ctx));
                }
                ctx.get().setPacketHandled(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
