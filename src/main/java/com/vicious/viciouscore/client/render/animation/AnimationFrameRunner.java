package com.vicious.viciouscore.client.render.animation;

import java.util.function.Supplier;

public abstract class AnimationFrameRunner {
    protected Supplier<Double> tx;
    protected Supplier<Double> ty;
    protected Supplier<Double> tz;
    protected Supplier<Double> rx;
    protected Supplier<Double> ry;
    protected Supplier<Double> rz;
    protected Supplier<Double> rxf;
    protected Supplier<Double> rxy;
    protected Supplier<Double> rxz;
}
