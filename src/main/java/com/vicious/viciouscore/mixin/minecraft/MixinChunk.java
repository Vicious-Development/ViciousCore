package com.vicious.viciouscore.mixin.minecraft;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin({Chunk.class})
public abstract class MixinChunk {
    @Shadow public abstract Map<BlockPos, TileEntity> getTileEntityMap();

    /**
     * Injects this method to prevent the tile from validating before its in the chunk.
     */
    @Inject(
            method = {"addTileEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntity;validate()V"
            )}
    )
    private void putInTileMapBeforeValidation(final BlockPos pos, final TileEntity tileEntityIn, final CallbackInfo ci) {
        getTileEntityMap().put(pos,tileEntityIn);
    }
}
