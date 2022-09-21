package com.vicious.viciouscore.common.capability.combined;

import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public interface ICombinedCapabilityProvider<T> {
    List<T> getProviders();
    void add(T t);
    void remove(T t);
    LazyOptional<ICombinedCapabilityProvider<T>> getLazyOptional();
}
