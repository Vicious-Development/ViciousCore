package com.vicious.viciouscore.common.item.structure;

import com.vicious.viciouscore.common.item.ViciousItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ItemStructureTool extends ViciousItem {
    public ItemStructureTool(String name) {
        super(name);
        setMaxStackSize(1);
    }
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return true;
    }
}
