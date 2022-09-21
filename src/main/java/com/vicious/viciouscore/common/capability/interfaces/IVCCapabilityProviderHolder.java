package com.vicious.viciouscore.common.capability.interfaces;

import com.vicious.viciouscore.common.capability.VCCapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IVCCapabilityProviderHolder extends ICapabilityProvider {
    VCCapabilityProvider getProvider();
}
