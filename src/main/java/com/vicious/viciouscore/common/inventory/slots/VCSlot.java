package com.vicious.viciouscore.common.inventory.slots;

import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class VCSlot extends Slot {
    protected RangedInteger zone;

    public VCSlot(Container container, int index, int xPosition, int yPosition, RangedInteger range) {
        super(container, index, xPosition, yPosition);
        zone = range;
        this.index = zone.firstIndex + index;
    }

    public RangedInteger getRange(){
        return zone;
    }
    public VCSlot moveSlot(int x, int y){
        return new VCSlot(container,index,x,y,zone);
    }
}
