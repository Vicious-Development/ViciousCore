package com.vicious.viciouscore.client.render;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRenderOverride extends ICCModelUser {
    /**
     * For when rendering on in the client's world while someone (entity or player) holds it.
     */
    @SideOnly(Side.CLIENT)
    void renderEntity();

    /**
     * For when rendering on the client's screen while held.
     * @param e
     */
    @SideOnly(Side.CLIENT)
    void renderClient(RenderSpecificHandEvent e);

    /**
     * Cancels an event if the overlay it renders is unnecessary.
     * @param e
     */
    @SideOnly(Side.CLIENT)
    void cancelRenderOverlays(RenderGameOverlayEvent e);

    @SideOnly(Side.CLIENT)
    default ItemTransformVec3f getTransform(ItemCameraTransforms.TransformType transformType){
        RenderItem
        return ItemCameraTransforms.DEFAULT.getTransform(transformType);
    }
}
