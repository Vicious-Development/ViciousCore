package com.vicious.viciouscore.common.util.mixinsupport;

import com.vicious.viciouscore.ViciousCore;

import java.util.function.Consumer;

public interface InjectionInterface {
    /**
     * Used outside of ViciousCore for mixin injection type casting. See ILLPlayerData in LifelossCore as an example.
     */
    default <V> void executeAs(Class<V> type, Consumer<V> cons){
        if(type.isInstance(this)) {
            cons.accept(type.cast(this));
        }
        else{
            ViciousCore.logger.error("Attempted to run code as unassignable type.");
            new Exception().printStackTrace();
        }
    }
}
