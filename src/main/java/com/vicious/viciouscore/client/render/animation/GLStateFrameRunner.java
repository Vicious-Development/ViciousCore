package com.vicious.viciouscore.client.render.animation;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class GLStateFrameRunner extends AnimationFrameRunner {
    private GLStateFrameRunner prevFrame;
    public GLStateFrameRunner(){}
    public GLStateFrameRunner setPrevious(GLStateFrameRunner previous){
        prevFrame=previous;
        return this;
    }
    public GLStateFrameRunner(GLStateFrameRunner previous){
        prevFrame=previous;
    }
    public abstract void run(float totalTicks);
    //Applies model changes made in the previous frame.
    protected void applyPreviousFrame(float totalticks){
        if(prevFrame == null) return;
        prevFrame.run(totalticks-1);
    }

    //Base level animations. Nothing really special, useful for avoiding repeat code.

    //Rotates the GLState at a variable rate when ran. Also works as a constant rotator if used in that way
    public static class Rotator extends GLStateFrameRunner{
        public Rotator(Supplier<Double> rotationx, Supplier<Double> rotationy, Supplier<Double> rotationz, Supplier<Double> x, Supplier<Double> y, Supplier<Double> z){
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.rxf =x;
            this.rxy =y;
            this.rxz =z;
        }
        public Rotator(Supplier<Double> rotationx, Supplier<Double> rotationy, Supplier<Double> rotationz, Supplier<Double> x, Supplier<Double> y, Supplier<Double> z, GLStateFrameRunner previous){
            super(previous);
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.rxf = x;
            this.rxy = y;
            this.rxz = z;
        }
        @Override
        public void run(float totalTicks) {
            applyPreviousFrame(totalTicks);
            if(!willRotate()) return;
            if(rxf != null) GlStateManager.rotate(totalTicks*rx.get().floatValue(), rxf.get().floatValue(),0,0);
            if(rxy != null) GlStateManager.rotate(totalTicks*ry.get().floatValue(), 0, rxy.get().floatValue(),0);
            if(rxz != null) GlStateManager.rotate(totalTicks*rz.get().floatValue(), 0,0, rxz.get().floatValue());
        }
        public boolean willRotate(){
            return !(rx == null && ry == null && rz == null) || !(rxf == null && rxy == null && rxz == null);
        }
    }
    //Translates the GLState at a variable rate when ran. Also works as a constant translator if used in that way
    public static class Translator extends GLStateFrameRunner{
        public Translator(Supplier<Double> translationx, Supplier<Double> translationy, Supplier<Double> translationz){
            tx=translationx;
            ty=translationy;
            tz=translationz;
        }
        public Translator(Supplier<Double> translationx, Supplier<Double> translationy, Supplier<Double> translationz, GLStateFrameRunner previousFrame) {
            super(previousFrame);
            tx=translationx;
            ty=translationy;
            tz=translationz;
        }
        @Override
        public void run(float totalTicks) {
            applyPreviousFrame(totalTicks);
            if(!willTranslate()) return;
            if(tx != null) GlStateManager.translate(totalTicks*tx.get(),0,0);
            if(ty != null) GlStateManager.translate(0, totalTicks*ty.get(),0);
            if(tz != null) GlStateManager.translate(0,0, totalTicks*tz.get());
        }
        public boolean willTranslate(){
            return !(tx == null && ty == null && tz == null);
        }
    }
    //Performs multiple frame transformations.
    public static class Multi extends GLStateFrameRunner{
        private List<GLStateFrameRunner> frames = new ArrayList<>();
        public Multi(GLStateFrameRunner... frames){
            this.frames.addAll(Arrays.asList(frames));
        }
        @Override
        public void run(float partialticks) {
            for (GLStateFrameRunner frame : frames) {
                frame.run(partialticks);
            }
        }
    }
}
