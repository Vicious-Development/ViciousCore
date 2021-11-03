package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

public class RenderEnergoRifle extends RenderModeledItem{
    public static CCModel defaultmodel = ViciousLoader.loadViciousModel("item/obj/energorifle.obj").backfacedCopy();
    @Override
    public CCModel getModel() {
        return defaultmodel;
    }

    @Override
    public void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        super.renderItem(item, transformType);
    }
    //NOTE TO SELF, IN RELATION TO PLAYER FRONT.
    // X+ rotates away from face. X- rotates towards.
    // Y+- rotates parallel to face.
    // Z+- Spins around real world y.
}
