package com.vicious.viciouscore.common.capability.interfaces;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface ICapabilityDeathPersistant extends IVCCapabilityHandler{
    void copyTo(ICapabilityProvider copy);
}
