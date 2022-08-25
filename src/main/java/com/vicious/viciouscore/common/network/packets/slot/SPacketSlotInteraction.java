package com.vicious.viciouscore.common.network.packets.slot;

import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSlotInteraction extends VCPacket {
    protected int slot;
    protected byte inventory;
    public SPacketSlotInteraction(int slot, byte inventory){
        this.slot=slot;
        this.inventory=inventory;
    }
    public SPacketSlotInteraction(FriendlyByteBuf buf){
        this.slot=buf.readInt();
        this.inventory=buf.readByte();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeByte(inventory);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ServerPlayer plr = ctx.getSender();
        if(plr != null){
            AbstractContainerMenu menu = plr.containerMenu;
            if(menu instanceof GenericContainer vccontainer){
                vccontainer.handleSlotPacket(this,plr);
            }
        }
    }
    public int getSlot(){
        return slot;
    }
    public int getInventory(){
        return inventory;
    }

    @Override
    public boolean handleOnServer() {
        return true;
    }

    public void handleSelf() {
        Player plr = Minecraft.getInstance().player;
        if(plr.containerMenu instanceof GenericContainer<?> cont){
            cont.handleSlotPacket(this,plr);
        }
    }
}
