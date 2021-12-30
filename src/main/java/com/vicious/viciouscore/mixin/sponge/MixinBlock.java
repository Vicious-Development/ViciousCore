package com.vicious.viciouscore.mixin.sponge;


import com.vicious.viciouscore.common.override.block.BlockOverrideHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = net.minecraft.block.Block.class, remap = false)
public class MixinBlock {
    /**
     * @author Drathonix
     */
    @Overwrite
    public boolean hasTileEntity(IBlockState state)
    {
        return this instanceof ITileEntityProvider || BlockOverrideHandler.hasTileInjector(state.getBlock());
    }
}
