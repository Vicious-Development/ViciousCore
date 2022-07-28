package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.capability.VCCapabilities;
import com.vicious.viciouscore.common.data.state.TickState;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public class SyncableTickState extends SyncableINBTCompound<TickState> {
    public SyncableTickState(String key) {
        super(key, new TickState(0));
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(VCCapabilities.TICKABLE);
    }
    public void tick(){
        value.tick();
        isDirty(true);
    }
    public void reset(){
        value.progress=0;
        isDirty(true);
    }
    public void setRequired(int ticks){
        value.completion=ticks;
        isDirty(true);
    }
    public double fractionOfTickTimeComplete() {
        return (double)value.progress/value.completion;
    }
    public int getProgress(){
        return value.progress;
    }
    public int getCompletion(){
        return value.completion;
    }
    public boolean hasCompleted(){
        return getProgress() >= getCompletion();
    }
}

