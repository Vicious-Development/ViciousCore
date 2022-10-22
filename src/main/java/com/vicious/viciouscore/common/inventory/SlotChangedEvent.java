package com.vicious.viciouscore.common.inventory;

import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import net.minecraft.world.item.ItemStack;

public class SlotChangedEvent {
    private final ItemStack stack;
    private final int slot;
    private Phase phase;
    private final IFastItemHandler handler;
    public SlotChangedEvent(ItemStack stack, Phase phase, int slot, IFastItemHandler handler) {
        this.stack = stack;
        this.slot = slot;
        this.handler = handler;
        this.phase=phase;
    }

    public ItemStack getStack(){
        return stack;
    }

    public enum Phase{
        POST,
        PRE
    }
}
