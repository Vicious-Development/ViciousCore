package com.vicious.viciouscore.common.util;

import java.util.List;

public class RangedInteger {
    public RangedInteger(int firstIndex, int length) {
        this.firstIndex = firstIndex;
        this.size = length;
        this.endex = firstIndex + length;
    }

    public final int firstIndex;
    public final int size;
    public final int endex;

    public static RangedInteger getRangeFromIndex(int index, List<RangedInteger> rangeList) {
        for (RangedInteger range : rangeList) {
            if (index >= range.firstIndex && index < range.endex) return range;
        }
        throw new IndexOutOfBoundsException("Unexpected index");
    }
    public boolean isInRange(int index){
        return index >= firstIndex && index < endex;
    }
    public String toString(){
        return "(" + firstIndex + "," + endex + ")";
    }
}
