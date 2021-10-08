package com.vicious.viciouscore.client.rendering.animation;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import com.vicious.viciouscore.client.rendering.ViciousRenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Used to create animations easily.
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unchecked")
public class Animation  {
    public float x,y,z;
    public float rotx,roty,rotz;
    public final AnimationFrameRunner[] frames;
    public Animation(int frameCount){
        frames = new AnimationFrameRunner[frameCount];
    }
    public CCModel runModelFrame(CCModel model, double x, double y, double z, float yaw, float partialticks){
        int modularFrame = (int) (calcTotalTicks(partialticks))%frames.length;
        if(frames[modularFrame] == null) return model;
        else{
            return ((CCModelFrameRunner)frames[modularFrame]).run(model,x,y,z,yaw,partialticks);
        }
    }
    public void runModelFrameAndRender(CCModel model, double x, double y, double z, float yaw, float partialticks, CCRenderState rs, Matrix4 mat){
        runModelFrame(model,x,y,z,yaw,partialticks).render(rs,mat);
    }
    public AnimationFrameRunner addFrame(int frame, AnimationFrameRunner in){
        frames[frame] = in;
        return in;
    }
    public float ppX(float in){
        float pre = x;
        x+=in;
        return pre;
    }
    public float ppY(float in){
        float pre = y;
        y+=in;
        return pre;
    }
    public float ppZ(float in){
        float pre = z;
        z+=in;
        return pre;
    }

    public float ppRotX(float in){
        float pre = rotx;
        rotx+=in;
        return pre;
    }
    public float ppRotY(float in){
        float pre = roty;
        roty+=in;
        return pre;
    }
    public float ppRotZ(float in){
        float pre = rotz;
        rotz+=in;
        return pre;
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
