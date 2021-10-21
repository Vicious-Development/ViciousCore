package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModel;
import com.vicious.viciouscore.common.util.ViciousLoader;

public class RenderEnergoRifle extends RenderModeledItem{
    public static CCModel defaultmodel = ViciousLoader.loadViciousModel("item/obj/energorifle.obj").backfacedCopy();
    @Override
    public CCModel getModel() {
        return defaultmodel;
    }
}
