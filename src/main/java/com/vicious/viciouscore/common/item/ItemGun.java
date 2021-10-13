package com.vicious.viciouscore.common.item;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.client.render.RenderEventManager;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Some guns will need us to render arms differently and override crosshairs.
 * IRenderOverride allows us to do that.
 */
public abstract class ItemGun extends Item implements IRenderOverride {
    public static CCModel defaultmodel = ViciousLoader.loadViciousModel("item/obj/energorifle.obj").backfacedCopy();

    public int getSightTime(){
        return 5; //Looks through sights in 5 ticks. (250ms on average).
    }

    /**
     * Client sided rendering code.
     */
    @Override
    public void renderEntity() {

    }
    @Override
    public void renderClient(RenderSpecificHandEvent in) {
        in.setCanceled(true);
        //Apply the camera transforms to the GLStateManager.
        ViciousRenderManager.applyCameraTransforms(this, in.ItemCamera);

        CCModel gun = getGunModel();
    }
    public void cancelRenderOverlays(RenderGameOverlayEvent e) {
        //Cancel rendering crosshairs if the player is using sights.
        if(e.getType().equals(RenderGameOverlayEvent.ElementType.CROSSHAIRS) && RenderEventManager.LOOKSIGHTSANIMATION.currentTick() == getSightTime()) e.setCanceled(true);
    }

    @SideOnly(Side.CLIENT)
    public void renderGun(Entity holder, ItemCameraTransforms.TransformType transformation, Vector3 camOffSet, float partialticks){
        //Get the weapon model
        CCModel gun = getGunModel();
        //Get the renderstate and bind the used texture.
        CCRenderState rs = startAndBind(getTexture());
        RenderEventManager.LOOKSIGHTSANIMATION.runModelFrameAndRender(gun,0,0,0, 0,partialticks,rs,getMatrix());
        rs.draw();
    }

    @SideOnly(Side.CLIENT)
    public CCModel getGunModel(){
        return defaultmodel;
    }
    @SideOnly(Side.CLIENT)
    public abstract ResourceLocation getTexture();
    @SideOnly(Side.CLIENT)
    protected Matrix4 getMatrix(double x, double y, double z){
        //Create the default rendering matrix (no rotation, scale of 1 block wide.
        return RenderUtils.getMatrix(new Vector3(x,y,z), new Rotation(0,0,0,1), 1).apply(getScale());
    }
}
