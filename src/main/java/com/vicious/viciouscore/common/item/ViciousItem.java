package com.vicious.viciouscore.common.item;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
    public BlockPos selectBlock(World worldIn, EntityPlayer playerIn){
         RayTraceResult res = this.rayTrace(worldIn, playerIn, true);
         if(res != null) return res.getBlockPos();
         return null;
    }
}
