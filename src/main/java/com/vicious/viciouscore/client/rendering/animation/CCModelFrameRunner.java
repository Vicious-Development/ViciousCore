package com.vicious.viciouscore.client.rendering.animation;

import codechicken.lib.render.CCModel;
import codechicken.lib.vec.Rotation;

import java.util.function.Supplier;

public abstract class CCModelFrameRunner implements AnimationFrameRunner {
    public abstract CCModel run(CCModel model, double x, double y, double z, float yaw, float partialticks);

    //Base level animations. Nothing really special, useful for avoiding repeat code.

    //Rotates an object at a constant rate when ran. Use VariableRotator to change rotation rates.
    public static class ConstantRotator extends CCModelFrameRunner{
        private double rx = 0;
        private double ry = 0;
        private double rz = 0;
        private double x = 0;
        private double y = 0;
        private double z = 0;

        public ConstantRotator(double rotationx, double rotationy, double rotationz, double x, double y, double z){
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.x=x;
            this.y=y;
            this.z=z;
        }
        @Override
        public CCModel run(CCModel model, double xin, double yin, double zin, float yaw, float partialticks) {
            float t = Animation.calcTotalTicks(partialticks);
            if(!willRotate()) return model;
            model = model.copy();
            if(x != 0) model.apply(new Rotation(t*rx, x,0,0));
            if(y != 0) model.apply(new Rotation(t*ry, 0,y,0));
            if(z != 0) model.apply(new Rotation(t*rz, 0,0,z));
            return model;
        }
        public boolean willRotate(){
            return !(rx == 0 && ry == 0 && rz == 0) || !(x==0 && y==0 && z == 0);
        }
    }

    //Rotates an object at a variable rate when ran. Also works as a constant rotator if used in that way
    public static class VariableRotator extends CCModelFrameRunner{
        private Supplier<Double> rx;
        private Supplier<Double> ry;
        private Supplier<Double> rz;
        private Supplier<Double> x;
        private Supplier<Double> y;
        private Supplier<Double> z;

        public VariableRotator(Supplier<Double> rotationx, Supplier<Double> rotationy, Supplier<Double> rotationz, Supplier<Double> x, Supplier<Double> y, Supplier<Double> z){
            rx=rotationx;
            ry=rotationy;
            rz=rotationz;
            this.x=x;
            this.y=y;
            this.z=z;
        }
        @Override
        public CCModel run(CCModel model, double xin, double yin, double zin, float yaw, float partialticks) {
            float t = Animation.calcTotalTicks(partialticks);
            if(!willRotate()) return model;
            model = model.copy();
            if(x != null) model.apply(new Rotation(t*rx.get(), x.get(),0,0));
            if(y != null) model.apply(new Rotation(t*ry.get(), 0,y.get(),0));
            if(z != null) model.apply(new Rotation(t*rz.get(), 0,0,z.get()));
            return model;
        }
        public boolean willRotate(){
            return !(rx == null && ry == null && rz == null) || !(x == null && y == null && z == null);
        }
    }
}
