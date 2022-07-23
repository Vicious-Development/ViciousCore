package com.vicious.viciouscore.common.inventory.slots;


import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotPlayerInv36 extends VCSlot {
    public SlotPlayerInv36(Container inventoryIn, int index, int xPosition, int yPosition, RangedInteger range) {
        super(inventoryIn, index, xPosition, yPosition, range);
    }
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return true;
    }
    public SlotPlayerInv36 moveSlot(int x, int y){
        return new SlotPlayerInv36(container, getSlotIndex(), x, y, zone);
    }
    public String toString(){
        return "inv:" + getSlotIndex() + ":" + zone.firstIndex;
    }
}
