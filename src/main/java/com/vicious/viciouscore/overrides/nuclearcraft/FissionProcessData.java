package com.vicious.viciouscore.overrides.nuclearcraft;

public class FissionProcessData {
    public double cooling;
    public double heatChange;
    public double processPower;

    public FissionProcessData(double heatChange, double cooling, double processPower) {
        this.heatChange=heatChange;
        this.cooling=cooling;
        this.processPower=processPower;
    }
}
