package com.vicious.viciouscore.common.capability.logistics;

import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a network of capabilities of the same type.
 */
public class CapabilityNetwork<T> {
    private final List<CapabilityConnection<T>> connections = new ArrayList<>();
    public void addConnection(LazyOptional<T> capability){
         CapabilityConnection<T> cc = new CapabilityConnection<>(capability);
         if(cc.valid()) connections.add(cc);
    }
    public void forEachConnection(Consumer<T> cons){
        for (int i = 0; i < connections.size(); i++) {
            CapabilityConnection<T> cc = connections.get(i);
            if(cc.valid()){
                T t = cc.get();
                if(t != null) cons.accept(t);
            }
            else{
                connections.remove(i);
                i--;
            }
        }
    }
    public void forEachConnection(Consumer<T> cons, Predicate<T> escape){
        for (int i = 0; i < connections.size(); i++) {
            CapabilityConnection<T> cc = connections.get(i);
            if(cc.valid()){
                T t = cc.get();
                if(t != null){
                    if(escape.test(t)) return;
                    cons.accept(t);
                }
            }
            else{
                connections.remove(i);
                i--;
            }
        }
    }
}
