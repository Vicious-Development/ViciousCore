package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.item.Item;

public class ViciousItem extends Item {
    public ViciousItem(){
        super();
        setCreativeTab(ViciousCore.TABVICIOUS);
    }
    public ViciousItem(String name){
        this();
        setUnlocalizedName(name);
        setRegistryName(ViciousCore.MODID,name);
    }
    public ViciousItem(String name, boolean doTab){
        setRegistryName(ViciousCore.MODID,name);
    }
}
