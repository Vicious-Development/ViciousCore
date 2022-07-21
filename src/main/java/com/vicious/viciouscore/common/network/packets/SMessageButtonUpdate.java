package com.vicious.viciouscore.common.network.packets;

import com.vicious.viciouscore.common.keybinding.CommonKeyBinding;
import com.vicious.viciouscore.common.keybinding.ServerButtonPressHandler;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SMessageButtonUpdate extends VCPacket {
    // A default constructor is always required
    public SMessageButtonUpdate(FriendlyByteBuf buf){
        toSend = buf.readInt();
        pressed = buf.readBoolean();
    }
    private int ticksExisted;
    private int toSend;
    private boolean pressed;
    public SMessageButtonUpdate(int toSend, boolean pressed) {
        this.toSend = toSend;
        this.pressed=pressed;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(toSend);
        buf.writeBoolean(pressed);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ServerPlayer plr = ctx.getSender();
        Map<Integer, CommonKeyBinding> bindings = ServerButtonPressHandler.bindings.get(plr);
        CommonKeyBinding binding = bindings.get(getCode());
        binding.isDown = isPressed();
        VCNetwork.getInstance().reply(ctx,new CMessageButtonPressReceived(getCode(),binding.isDown));
        ctx.setPacketHandled(true);
    }

    public int getCode(){
        return toSend;
    }

    public boolean isPressed(){
        return pressed;
    }
    public int getTicksExisted(){return ticksExisted;}
    public void tick(){ticksExisted++;}
}

