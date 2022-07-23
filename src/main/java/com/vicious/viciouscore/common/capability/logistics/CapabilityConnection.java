package com.vicious.viciouscore.common.capability.logistics;

import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityConnection<T> {
    private LazyOptional<T> optional;
    private boolean valid;
    public CapabilityConnection(LazyOptional<T> optional){
        this.optional=optional;
        valid=optional.isPresent();
        optional.addListener(this::onInvalidate);
    }
    public void onInvalidate(LazyOptional<T> opt){
        valid=false;
    }
    public boolean valid(){
        return valid;
    }
    public T get(){
        return FuckLazyOptionals.getOrNull(optional);
    }
}
