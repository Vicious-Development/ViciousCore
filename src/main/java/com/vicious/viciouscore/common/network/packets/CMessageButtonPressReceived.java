package com.vicious.viciouscore.common.network.packets;

import com.vicious.viciouscore.common.VCoreConfig;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CMessageButtonPressReceived extends VCPacket {
    private int toSend;
    private boolean pressed;
    public CMessageButtonPressReceived(int toSend, boolean pressed) {
        this.toSend = toSend;
        this.pressed=pressed;
    }

    public CMessageButtonPressReceived(FriendlyByteBuf buf) {
        toSend = buf.readInt();
        pressed = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(toSend);
        buf.writeBoolean(pressed);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        Handler.getInstance().onMessage(this,context);
        ctx.setPacketHandled(true);
    }

    public int getCode(){
        return toSend;
    }
    public boolean isPressed(){
        return pressed;
    }
    public static class Handler{
        private static Handler instance;
        public static Handler getInstance(){
            if(instance == null) instance = new Handler();
            return instance;
        }
        public Map<Integer, SMessageButtonUpdate> toSend = new HashMap<>();
        private boolean hasGottenResponse = false;
        public void onMessage(CMessageButtonPressReceived message, Supplier<NetworkEvent.Context> ctx) {
            if(toSend.containsKey(message.getCode())) {
                if (toSend.get(message.getCode()).isPressed() == message.isPressed()) {
                    toSend.remove(message.getCode());
                    hasGottenResponse = true;
                }
            }
        }
        public void startSending(SMessageButtonUpdate buttonPacket){
            //Reflection.invokeMethod(toSend,CommonKeyBindings.grow, new Object[]{buttonPacket.getCode() + 1});
            toSend.put(buttonPacket.getCode(),buttonPacket);
        }
        public void tick(){
            for (int i : toSend.keySet()) {
                if(hasGottenResponse){
                    hasGottenResponse = false;
                    return;
                }
                SMessageButtonUpdate pk = toSend.get(i);
                if(pk == null) continue;
                if(pk.getTicksExisted() >= VCoreConfig.getInstance().buttonPressResponseTimeOut.value()){
                    toSend.remove(i);
                }
                pk.tick();
                VCNetwork.getInstance().sendToServer(pk);
            }
        }
    }
}
