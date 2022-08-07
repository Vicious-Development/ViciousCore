package com.vicious.viciouscore.common.inventory.container;

import com.vicious.viciouscore.common.util.item.ItemHelper;
import net.minecraft.world.item.ItemStack;

public class UserInteractionState {
    protected ItemStack held = ItemStack.EMPTY;

    public boolean isHolding(ItemStack slotStack) {
        return ItemHelper.doItemsMatch(held,slotStack);
    }

    public ItemStack getHeld() {
        return held;
    }
    /**
     * Shift left/right: switch inventory
     *
     * Held Empty:
     * 	-Right: take whole stack.
     * 	-Left: split stack, transfer stack gets more or equal.
     *
     * Held Not Empty:
     * 	-Same type
     * 		-Right: Place maximum
     * 		-Left: Place one
     * 	-Different type:
     * 		-Right/left: Swap stacks
     * Quickbinds
     * 	-Double click stack: maximize stack. (shift clicking conflicts)
     * 	-Drop Key - drop, hold ctrl: drop all.
     * 	-Click stack outside GUI: drop
     *
     */
}
