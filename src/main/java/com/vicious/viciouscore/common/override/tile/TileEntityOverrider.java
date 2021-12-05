package com.vicious.viciouscore.common.override.tile;

import com.vicious.viciouscore.common.override.OverrideHandler;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Function;

public class TileEntityOverrider extends OverrideHandler {
    public final Function<TileEntity, TileEntity> func;
    public TileEntityOverrider(Function<TileEntity, TileEntity> func, String applier, String modidTarget) {
        super(applier, modidTarget);
        this.func = func;
    }

    public TileEntityOverrider(Function<TileEntity, TileEntity> func, String applier, String modidTarget, boolean required) {
        super(applier, modidTarget, required);
        this.func = func;
    }

    public TileEntityOverrider(Function<TileEntity, TileEntity> func, String applier, String modidTarget, boolean required, int priority) {
        super(applier, modidTarget, required, priority);
        this.func = func;
    }
    public TileEntityOverrider(Function<TileEntity, TileEntity> func, String applier, String modidTarget, int priority) {
        super(applier, modidTarget, priority);
        this.func = func;
    }
    public String toString(){
        return super.toString();
    }
}
