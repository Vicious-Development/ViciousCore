package com.vicious.viciouscore.common.network.packets.keybindpress;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.capability.types.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketButtonUpdate extends VCPacket {
    // A default constructor is always required
    public SPacketButtonUpdate(FriendlyByteBuf buf){
        toSend = buf.readInt();
        pressed = buf.readBoolean();
    }
    @OnlyIn(Dist.CLIENT)
    private int ticksExisted;
    private int toSend;
    private boolean pressed;
    public SPacketButtonUpdate(int toSend, boolean pressed) {
        this.toSend = toSend;
        this.pressed=pressed;
    }

    @Override
    public boolean handleOnServer() {
        return true;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(toSend);
        buf.writeBoolean(pressed);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        try {
            NetworkEvent.Context ctx = context.get();
            ServerPlayer plr = ctx.getSender();
            if (plr == null) return;
            KeyPressHandler handler = VCCapabilities.getCapability(plr, KeyPressHandler.class);
            if (handler == null) return;
            handler.setDown(getCode(), isPressed());
            VCNetwork.getInstance().reply(ctx, new CPacketButtonPressReceived(getCode(), isPressed()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCode(){
        return toSend;
    }
    public boolean isPressed(){
        return pressed;
    }
    @OnlyIn(Dist.CLIENT)
    public int getTicksExisted(){return ticksExisted;}
    @OnlyIn(Dist.CLIENT)
    public void tick(){ticksExisted++;}
}

