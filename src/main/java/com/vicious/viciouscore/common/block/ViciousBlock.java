package com.vicious.viciouscore.common.block;


import com.vicious.viciouscore.ViciousCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;

public class ViciousBlock extends Block {
    protected BlockStateContainer blockState;

    public ViciousBlock(String name, Material materialIn) {
        super(materialIn);
        setCreativeTab(ViciousCore.TABVICIOUS);
        setUnlocalizedName(name);
        setRegistryName(name);

        this.blockState = this.getBlockState();
    }
    public ViciousBlock(String name) {
        this(name, Material.AIR);
    }
}
