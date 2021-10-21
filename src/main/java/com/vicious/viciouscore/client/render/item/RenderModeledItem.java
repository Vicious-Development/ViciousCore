package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.client.render.ICCModelUser;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public abstract class RenderModeledItem implements ICCModelUser, ICCModelConsumer, IItemRenderer {
    @Override
    public void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        Matrix4 mat = getMatrix(0,0,0);
        GlStateManager.pushMatrix();
        GlStateTracker.pushState();
        //Start drawing
        CCRenderState rs = startAndBind(getModelTexture());

        //Render Model
        getAnimation().runModelFrameAndRender(getModel(),0,0,0,0,0,rs,mat);

        //Draw
        rs.draw();
        GlStateTracker.popState();
        GlStateManager.popMatrix();
    }

    @Override
    public IModelState getTransforms() {
        return TransformUtils.DEFAULT_ITEM;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public ResourceLocation getModelTexture() {
        return ResourceCache.ENERGORIFLELOCATION;
    }
}
