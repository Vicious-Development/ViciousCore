package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.structures.SyncableINBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class SyncableEnergyStorage extends SyncableINBT<EnergyStorage> implements IEnergyStorage {
    public SyncableEnergyStorage(String KEY, int capacity) {
        super(KEY, new EnergyStorage(capacity));
    }
    public SyncableEnergyStorage(String KEY, EnergyStorage storage) {
        super(KEY, storage);
    }

    @Override
    protected List<Capability<?>> getCapabilityTokens() {
        return List.of(CapabilityEnergy.ENERGY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return value.receiveEnergy(maxReceive,simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return value.extractEnergy(maxExtract,simulate);
    }

    @Override
    public int getEnergyStored() {
        return value.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return value.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return value.canExtract();
    }

    @Override
    public boolean canReceive() {
        return value.canReceive();
    }

}
