package com.vicious.viciouscore.overrides.nuclearcraft;

import nc.gui.generator.GuiFissionController;
import nc.tile.generator.TileFissionController;
import net.minecraft.entity.player.EntityPlayer;

public class GuiOverrideFissionController extends GuiFissionController {
    public GuiOverrideFissionController(EntityPlayer player, TileFissionController tile) {
        super(player, tile);
    }
}
