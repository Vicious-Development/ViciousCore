package com.vicious.viciouscore.common.data.implementations.attachable;

import com.vicious.viciouscore.common.data.GlobalData;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.util.mixinsupport.InjectionInterface;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.function.Consumer;

public class SyncableGlobalData extends SyncableCompound implements InjectionInterface {
    public SyncableGlobalData() {
        super("globaldata");
    }

    public static void executeIfPresent(Object o, Consumer<SyncableGlobalData> cons){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons);
        }
    }
    public static <V> void executeIfPresent(Object o, Consumer<V> cons, Class<V> as){
        if(o instanceof ICapabilityProvider p){
            executeIfPresent(p,cons,as);
        }
    }
    public static void executeIfPresent(ICapabilityProvider p, Consumer<SyncableGlobalData> cons){
        SyncableGlobalData data = GlobalData.getGlobalData();
        if(data != null){
            cons.accept(data);
        }
    }
    public static <V> void executeIfPresent(ICapabilityProvider p, Consumer<V> cons, Class<V> as){
        SyncableGlobalData data = GlobalData.getGlobalData();
        if(as.isInstance(data)){
            cons.accept(as.cast(data));
        }
    }
}
