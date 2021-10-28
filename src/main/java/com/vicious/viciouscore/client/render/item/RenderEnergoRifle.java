package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModel;
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
        AbstractClientPlayer abstractClientPlayer = Minecraft.getMinecraft().player;
        Minecraft.getMinecraft().getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        RenderPlayer renderPlayer = (RenderPlayer) Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(abstractClientPlayer);

        GlStateManager.disableCull();
        ModelRenderer arm = renderPlayer.getMainModel().bipedRightArm;

        arm.rotateAngleX = 0F;
        arm.rotateAngleY = 0F;
        arm.rotateAngleZ = 0F;
        arm.render(1F);

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    @Override
    public Scale getScale() {
        return new Scale(0.125,0.125,0.125);
    }
    public Rotation getRotation(){
        return new Rotation(-90,1,0,0);
    }
    //NOTE TO SELF, IN RELATION TO PLAYER FRONT.
    // X+ rotates away from face. X- rotates towards.
    // Y+- rotates parallel to face.
    // Z+- Spins around real world y.
}
