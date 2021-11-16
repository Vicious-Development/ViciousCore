package com.vicious.viciouscore.common.item.structure;

import com.vicious.viciouscore.common.player.ViciousCorePlayerInfo;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.structure.ViciousCoreStructureSystem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemStructurePasteWand extends ItemStructureTool {
    public ItemStructurePasteWand(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        paste(playerIn);
        return ActionResult.newResult(EnumActionResult.FAIL,playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(!(entityLiving instanceof EntityPlayer)) return false;
        paste((EntityPlayer) entityLiving);
        return false;
    }
    public void paste(EntityPlayer pIn){
        ViciousCorePlayerInfo pinfo = ViciousCorePlayerManager.getPInfo(pIn.getUniqueID());
        pinfo.pos1 = selectBlock(pIn.getEntityWorld(),pIn);
        pinfo.world = pIn.world;
        ViciousCoreStructureSystem.pasteStructure(pIn);
    }
}
