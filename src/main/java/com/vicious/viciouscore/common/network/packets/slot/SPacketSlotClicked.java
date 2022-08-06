package com.vicious.viciouscore.common.network.packets.slot;

import net.minecraft.network.FriendlyByteBuf;

public class SPacketSlotClicked extends SPacketSlotInteraction {
    protected int button;
    protected boolean shiftHeld;
    public SPacketSlotClicked(int slot, byte inventory, int button, boolean shiftHeld){
        super(slot, inventory);
        this.button = button;
        this.shiftHeld=shiftHeld;
    }
    public SPacketSlotClicked(FriendlyByteBuf buf){
        super(buf);
        this.button = buf.readInt();
        this.shiftHeld = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(button);
        buf.writeBoolean(shiftHeld);
    }
    public boolean isLeftClick(){
        return button == 0;
    }
    public boolean shiftHeld(){
        return shiftHeld;
    }
}
