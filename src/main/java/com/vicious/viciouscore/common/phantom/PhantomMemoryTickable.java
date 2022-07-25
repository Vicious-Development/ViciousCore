package com.vicious.viciouscore.common.phantom;

public abstract class PhantomMemoryTickable extends PhantomMemory{
    public abstract void tick();
    public abstract boolean shouldTick();
}
