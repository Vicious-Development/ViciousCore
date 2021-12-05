package com.vicious.viciouscore.common.override.block;

import com.vicious.viciouscore.common.override.OverrideHandler;
import net.minecraft.block.Block;

class BlockOverrider extends OverrideHandler {
    public final Block block;

    public BlockOverrider(Block block, String applier, String modidTarget) {
        super(applier, modidTarget);
        this.block = block;
    }

    public BlockOverrider(Block block, boolean required, String applier, String modidTarget) {
        super(applier, modidTarget, required);
        this.block = block;
    }

    public BlockOverrider(Block block, String applier, String modidTarget, boolean required, int priority) {
        super(applier, modidTarget, required, priority);
        this.block = block;
    }
}
