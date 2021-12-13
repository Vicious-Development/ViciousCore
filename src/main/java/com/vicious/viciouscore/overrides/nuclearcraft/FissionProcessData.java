package com.vicious.viciouscore.overrides.nuclearcraft;

public class FissionProcessData {
    public double passiveCooling;
    public double passiveHeat;
    public double processPower;

    public FissionProcessData(double passiveHeat, double passiveCooling, double processPower) {
        this.passiveHeat=passiveHeat;
        this.passiveCooling=passiveCooling;
        this.processPower=processPower;
    }
}
