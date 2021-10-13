package com.vicious.viciouscore.client.render.animation;

public class TickableAnimation extends Animation{
    private int ticksStored = 0;
    public TickableAnimation(int frameCount) {
        super(frameCount);
    }
    public void nextTick(){
        ticksStored++;
    }
    public void prevTick(){
        ticksStored--;
    }
    public void zeroTicks(){
        ticksStored = Math.max(0, ticksStored--);
    }
    public void zeroTicks(int dec){
        ticksStored = Math.max(0, ticksStored-dec);
    }
    public void setTicks(int tick){
        ticksStored=tick;
    }

    public int currentTick() {
        return ticksStored;
    }
}
