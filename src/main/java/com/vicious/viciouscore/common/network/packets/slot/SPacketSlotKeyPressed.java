package com.vicious.viciouscore.common.network.packets.slot;

import net.minecraft.network.FriendlyByteBuf;

public class SPacketSlotKeyPressed extends SPacketSlotInteraction{
    protected int key;

    public SPacketSlotKeyPressed(int slot, byte inventory, int key) {
        super(slot,inventory);
        this.key=key;
    }

    public SPacketSlotKeyPressed(FriendlyByteBuf buf) {
        super(buf);
        buf.writeInt(key);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        this.key=buf.readInt();
    }
    public boolean wasKeyPressed(int key){
        return this.key == key;
    }
}
