package com.vicious.viciouscore.common.inventory;

import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import net.minecraft.world.item.ItemStack;

public class SlotChangedEvent {
    private final ItemStack stack;
    private final int slot;
    private Phase phase;
    private final IFastItemHandler handler;
    private final Action action;
    public SlotChangedEvent(ItemStack stack, Phase phase, Action action, int slot, IFastItemHandler handler) {
        this.stack = stack;
        this.slot = slot;
        this.handler = handler;
        this.phase=phase;
        this.action=action;
    }

    public ItemStack getStack(){
        return stack;
    }

    public int getSlot() {
        return slot;
    }

    public Phase getPhase() {
        return phase;
    }

    public IFastItemHandler getInventory() {
        return handler;
    }

    public Action getAction() {
        return action;
    }


    public enum Phase{
        POST,
        PRE
    }
    public enum Action{
        EXTRACT,
        INSERT,
        SET
    }
}
