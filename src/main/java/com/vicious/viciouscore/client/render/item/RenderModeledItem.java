package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModelState;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.item.CCRenderItem;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.client.render.ICCModelUser;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.Sys;

import javax.vecmath.Matrix4f;
import java.util.HashMap;
import java.util.Map;

public abstract class RenderModeledItem implements ICCModelUser, ICCModelConsumer, IItemRenderer {
    @Override
    public void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        System.out.println("TT:" + transformType);
        GlStateManager.pushMatrix();
        GlStateTracker.pushState();

        Matrix4 mat = getMatrix();
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
        Map<ItemCameraTransforms.TransformType, TRSRTransformation> map = new HashMap<>();
        TRSRTransformation thirdPerson = TransformUtils.create(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
        TRSRTransformation firstPerson = TransformUtils.create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
        map.put(ItemCameraTransforms.TransformType.GROUND, TransformUtils.create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemCameraTransforms.TransformType.HEAD, TransformUtils.create(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        map.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, TransformUtils.flipLeft(thirdPerson));
        map.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
        map.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, TransformUtils.flipLeft(firstPerson));
        CCModelState DEFAULTRIFLE = new CCModelState(map);
        return DEFAULTRIFLE;
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
