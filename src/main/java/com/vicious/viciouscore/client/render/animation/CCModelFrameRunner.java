package com.vicious.viciouscore.client.render.animation;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Translation;
import com.vicious.viciouscore.client.configuration.ItemTransformOverrideCFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class CCModelFrameRunner extends AnimationFrameRunner {
    private CCModelFrameRunner prevFrame;
    public CCModelFrameRunner(){}
    public CCModelFrameRunner setPrevious(CCModelFrameRunner previous){
        prevFrame=previous;
        return this;
    }
    public CCModelFrameRunner(CCModelFrameRunner previous){
        prevFrame=previous;
    }
    public abstract CCModel run(CCModel model, double x, double y, double z, float yaw, float partialticks);
    //Applies model changes made in the previous frame.
    protected CCModel applyPreviousFrame(CCModel model, double x, double y, double z, float yaw, float partialticks){
        if(prevFrame == null) return model;
        return prevFrame.run(model,x,y,z,yaw,partialticks-1);
    }

    //Base level animations. Nothing really special, useful for avoiding repeat code.

    //Rotates an object at a variable rate when ran. Also works as a constant rotator if used in that way
    public static class Rotator extends CCModelFrameRunner{
        public Rotator(Supplier<Double> rotationx, Supplier<Double> rotationy, Supplier<Double> rotationz, Supplier<Double> x, Supplier<Double> y, Supplier<Double> z){
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.rxf =x;
            this.rxy =y;
            this.rxz =z;
        }
        public Rotator(Supplier<Double> rotationx, Supplier<Double> rotationy, Supplier<Double> rotationz, Supplier<Double> x, Supplier<Double> y, Supplier<Double> z, CCModelFrameRunner previous){
            super(previous);
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.rxf =x;
            this.rxy =y;
            this.rxz =z;
        }
        @Override
        public CCModel run(CCModel model, double xin, double yin, double zin, float yaw, float totalTicks) {
            model = applyPreviousFrame(model,xin,yin,zin,yaw,totalTicks);
            if(!willRotate()) return model;
            model = model.copy();
            if(rxf != null) model.apply(new Rotation(totalTicks*rx.get(), rxf.get(),0,0));
            if(rxy != null) model.apply(new Rotation(totalTicks*ry.get(), 0, rxy.get(),0));
            if(rxz != null) model.apply(new Rotation(totalTicks*rz.get(), 0,0, rxz.get()));
            return model;
        }
        public boolean willRotate(){
            return !(rx == null && ry == null && rz == null) || !(rxf == null && rxy == null && rxz == null);
        }
    }
    //Translates an object at a variable rate when ran. Also works as a constant translator if used in that way
    public static class Translator extends CCModelFrameRunner{
        public Translator(Supplier<Double> translationx, Supplier<Double> translationy, Supplier<Double> translationz){
            tx=translationx;
            ty=translationy;
            tz=translationz;
        }
        public Translator(Supplier<Double> translationx, Supplier<Double> translationy, Supplier<Double> translationz, CCModelFrameRunner previousFrame) {
            super(previousFrame);
            tx=translationx;
            ty=translationy;
            tz=translationz;
        }
        @Override
        public CCModel run(CCModel model, double xin, double yin, double zin, float yaw, float totalTicks) {
            model = applyPreviousFrame(model,xin,yin,zin,yaw,totalTicks);
            if(!willTranslate()) return model;
            model = model.copy();
            if(tx != null) model.apply(new Translation(totalTicks*tx.get(),0,0));
            if(ty != null) model.apply(new Translation(0,totalTicks*ty.get(),0));
            if(tz != null) model.apply(new Translation(0,0,totalTicks*tz.get()));
            return model;
        }
        public boolean willTranslate(){
            return !(tx == null && ty == null && tz == null);
        }
    }
    //Performs multiple frame transformations.
    public static class Multi extends CCModelFrameRunner{
        private List<CCModelFrameRunner> frames = new ArrayList<>();
        public Multi(CCModelFrameRunner... frames){
            this.frames.addAll(Arrays.asList(frames));
        }
        @Override
        public CCModel run(CCModel model, double x, double y, double z, float yaw, float partialticks) {
            for (CCModelFrameRunner frame : frames) {
                model = frame.run(model,x,y,z,yaw,partialticks);
            }
            return model;
        }
    }

    public static class Configurate {
        private ItemTransformOverrideCFG cfg;

        public Configurate(ItemTransformOverrideCFG r) {
            cfg = r;
        }

        public CCModel run(CCModel model, double x, double y, double z, float yaw, float partialticks) {
            if(!cfg.active.getBoolean()) return model;
            model = model.copy();
            if(cfg.overrideRotation.getBoolean()){
                model.apply(new Rotation(Math.toRadians(cfg.rx.value()),1,0,0));
                model.apply(new Rotation(Math.toRadians(cfg.ry.value()),0,1,0));
                model.apply(new Rotation(Math.toRadians(cfg.rz.value()),0,0,1));
            }
            if(cfg.overrideTranslation.getBoolean()){
                model.apply(new Translation(cfg.tx.value(),cfg.ty.value(),cfg.tz.value()));
            }
            if(cfg.overrideScale.getBoolean()){
                model.apply(new Scale(cfg.sx.value(),cfg.sy.value(),cfg.sz.value()));
            }
            return model;
        }
    }
}
