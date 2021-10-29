package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
        GlStateManager.pushMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public Scale getScale() {
        return new Scale(0.11,0.11,0.11);
    }
    public Rotation getRotation(){
        return new Rotation(30,-1,0,0);
    }

    @Override
    public Matrix4 getMatrix() {
        return super.getMatrix().translate(0,0,0);
    }
    //NOTE TO SELF, IN RELATION TO PLAYER FRONT.
    // X+ rotates away from face. X- rotates towards.
    // Y+- rotates parallel to face.
    // Z+- Spins around real world y.
}
