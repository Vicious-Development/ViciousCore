package com.vicious.viciouscore.common.inventory.slots;

import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class VCSlot extends Slot {
    protected RangedInteger zone;

    public VCSlot(Container container, int index, int xPosition, int yPosition, RangedInteger range) {
        super(container, index, xPosition, yPosition);
        zone = range;
    }

    @Override
    public int getSlotIndex() {
        return zone.firstIndex+super.getSlotIndex();
    }
    public int getActualIndex() {
        return super.getSlotIndex();
    }

    public RangedInteger getRange(){
        return zone;
    }
    public VCSlot moveSlot(int x, int y){
        return new VCSlot(container,getActualIndex(),x,y,zone);
    }
}
