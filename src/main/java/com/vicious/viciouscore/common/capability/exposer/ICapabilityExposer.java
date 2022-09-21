package com.vicious.viciouscore.common.capability.exposer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface ICapabilityExposer extends ICapabilityProvider {
    EmptyExposer EMPTY = new EmptyExposer();
    <T> void expose(T prov);

    <T> void expose(Capability<T> type, T prov);

    <T> void conceal(T prov);

    <T> void conceal(Capability<T> type, T prov);
}