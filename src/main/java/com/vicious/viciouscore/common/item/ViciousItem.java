package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.item.Item;

public class ViciousItem extends Item {
    public ViciousItem(){
        super();
        setRegistryName(ViciousCore.MODID);
    }
    public ViciousItem(String name){
        setUnlocalizedName(name);
        setRegistryName(ViciousCore.MODID,name);
    }
}
