package com.vicious.viciouscore.common.item.structure;

import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemStructureAreaSelectionWand extends ItemStructureTool {
    public ItemStructureAreaSelectionWand(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ViciousCorePlayerManager.getPInfo(playerIn.getUniqueID()).world = playerIn.getEntityWorld();
        ViciousCorePlayerManager.getPInfo(playerIn.getUniqueID()).pos2 = selectBlock(worldIn,playerIn);
        BlockPos pos2 = ViciousCorePlayerManager.getPInfo(playerIn.getUniqueID()).pos2;
        playerIn.sendMessage(new TextComponentString("Pos2 set to (" + pos2.getX() + "," + pos2.getY() + "," + pos2.getZ() + ")"));
        return ActionResult.newResult(EnumActionResult.FAIL,playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(!(entityLiving instanceof EntityPlayer)) return false;
        ViciousCorePlayerManager.getPInfo(entityLiving.getUniqueID()).pos1 = selectBlock(entityLiving.getEntityWorld(), (EntityPlayer) entityLiving);
        ViciousCorePlayerManager.getPInfo(entityLiving.getUniqueID()).world = entityLiving.getEntityWorld();

        BlockPos pos1 = ViciousCorePlayerManager.getPInfo(entityLiving.getUniqueID()).pos1;
        entityLiving.sendMessage(new TextComponentString("Pos1 set to (" + pos1.getX() + "," + pos1.getY() + "," + pos1.getZ() + ")"));
        return false;
    }
}
