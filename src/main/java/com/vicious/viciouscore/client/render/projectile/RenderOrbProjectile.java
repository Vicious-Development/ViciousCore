package com.vicious.viciouscore.client.render.projectile;

import codechicken.lib.render.CCModel;
import com.vicious.viciouscore.client.render.animation.Animation;
import com.vicious.viciouscore.client.render.animation.CCModelFrameRunner;
import com.vicious.viciouscore.common.entity.projectile.OrbProjectile;
import com.vicious.viciouscore.common.util.ViciousLoader;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Generic orb renderer. Uses a basic .Obj models. Moves as expected, nothing really special.
 * To create custom effects on this projectile, just extend this class and override doRender();
 * To override the models used, override method getModel().
 * @param <T>
 */
@SideOnly(Side.CLIENT)
public class RenderOrbProjectile<T extends OrbProjectile> extends RenderModeledProjectile<T>{
    //Store a generic orb models.
    public static CCModel defaultmodel = ViciousLoader.loadViciousModel("projectile/obj/orb.obj").backfacedCopy();
    //Monoframe rotation animation. Movement and scale is handled by the server.
    protected static Animation anim = Animation.newSingleFrame(new CCModelFrameRunner.Rotator(
                ()->0.1,
                ()->0.1,
                ()->0.1,
                ()-> 1.0,
                ()-> 1.0,
                ()-> 1.0
        ));
    public RenderOrbProjectile(RenderManager renderManager) {
        super(renderManager);
    }
    public CCModel getModel(){
        return defaultmodel;
    }
}
