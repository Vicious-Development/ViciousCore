package com.vicious.viciouscore.client.render.item;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.configuration.ClientOverrideConfigurations;
import com.vicious.viciouscore.client.configuration.HeldItemOverrideCFG;
import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.client.render.ICCModelUser;
import com.vicious.viciouscore.client.render.animation.CCModelFrameRunner;
import com.vicious.viciouscore.client.configuration.ItemTransformOverrideCFG;
import com.vicious.viciouscore.common.util.ResourceCache;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public abstract class RenderModeledItem implements ICCModelUser, ICCModelConsumer, IItemRenderer {
    @Override
    public void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        GlStateManager.pushMatrix();
        GlStateTracker.pushState();
        HeldItemOverrideCFG cfg = ClientOverrideConfigurations.getWhenHeldOverride(item.getItem());
        ItemTransformOverrideCFG r = cfg.getItemConfig();
        Matrix4 mat = getMatrix();
        //Start drawing
        CCRenderState rs = startAndBind(getModelTexture());

        //Render Model
        CCModel mdl = new CCModelFrameRunner.Configurate(r,transformType).run(getModel(),0.0,0.0,0.0,0.0f,0.0f);
        getAnimation().runModelFrameAndRender(mdl,0,0,0,0,0,rs,mat);

        //Draw
        rs.draw();
        GlStateTracker.popState();
        GlStateManager.popMatrix();
    }

    @Override
    public IModelState getTransforms() {
        /*Map<ItemCameraTransforms.TransformType, TRSRTransformation> map = new HashMap<>();
        TRSRTransformation thirdPerson = TransformUtils.create(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
        TRSRTransformation firstPerson = TransformUtils.create(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
        map.put(ItemCameraTransforms.TransformType.GROUND, TransformUtils.create(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
        map.put(ItemCameraTransforms.TransformType.HEAD, TransformUtils.create(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
        map.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
        map.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, TransformUtils.flipLeft(thirdPerson));
        map.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
        map.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, TransformUtils.flipLeft(firstPerson));
        CCModelState DEFAULTRIFLE = new CCModelState(map);*/
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
