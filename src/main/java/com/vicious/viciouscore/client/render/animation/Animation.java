package com.vicious.viciouscore.client.render.animation;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Used to create animations easily.
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unchecked")
public class Animation  {
    public final AnimationFrameRunner[] frames;
    public Animation(int frameCount){
        frames = new AnimationFrameRunner[frameCount];
    }
    public static <T extends Animation> T newSingleFrame(AnimationFrameRunner runner){
        Animation a = new Animation(1);
        a.addFrame(0,runner);
        return (T)a;
    }

    public static Animation empty() {
        return new Animation(0);
    }

    public CCModel runModelFrame(CCModel model, double x, double y, double z, float yaw, float partialticks){
        //Empty animation, this is usually when getAnimation is not overriden.
        if(frames.length == 0) return model;
        int modularFrame = (int) (calcTotalTicks(partialticks))%frames.length;
        if(frames[modularFrame] == null) return model;
        else{
            return ((CCModelFrameRunner)frames[modularFrame]).run(model,x,y,z,yaw,calcTotalTicks(partialticks));
        }
    }
    public void runModelFrameAndRender(CCModel model, double x, double y, double z, float yaw, float partialticks, CCRenderState rs, Matrix4 mat){
        runModelFrame(model,x,y,z,yaw,partialticks).render(rs,mat);
    }
    public AnimationFrameRunner addFrame(int frame, AnimationFrameRunner in){
        frames[frame] = in;
        return in;
    }

    //Assumes that the rotation is a constant speed. Useful for objects that spin constantly.
    public static float getRotationFromTicks(float partialTicks, float factor){
        return calcTotalTicks(partialTicks)*factor;
    }

    public static float calcTotalTicks(float partialTicks){
        return ViciousRenderManager.tickTimeElasped + partialTicks;
    }

    public void addFrameInRange(int start, int end, AnimationFrameRunner frame) {
        for (int i = start; i < end; i++) {
            frames[i]=frame;
        }
    }
}
