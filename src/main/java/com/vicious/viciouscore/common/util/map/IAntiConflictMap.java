package com.vicious.viciouscore.common.util.map;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.util.identification.IModIdentifiable;

import java.util.Map;

public interface IAntiConflictMap<K,V extends IModIdentifiable> extends Map<K,V> {
    default void putOrThrow(K k, V v){
        if(!containsKey(k)){
            put(k,v);
        }
        else if(v != null){
            V val = get(k);
            if(val.equals(v)){
                ViciousCore.logger.warn(v.getModid() + " attempted to register a " + v.getClass().getName() + " to a key which already has the equivalent value registered.");
            }
            else {
                if (val.getModid().equals(ViciousCore.MODID)) {
                    throw new IllegalStateException(v.getModid() + " attempted to register a " + val.getClass().getName() + " that ViciousCore already provides." +
                            "\n Please report this crash to the " + v.getModid() + " issue tracker and disable the mod until a patch is provided if possible.");
                }
                else {
                    throw new IllegalStateException(v.getModid() + " attempted to register a " + val.getClass().getName() + " that " + val.getModid() + " already provides. " +
                            "\n Please report this crash to both the " + v.getModid() + " and " + val.getModid() + " issue trackers and disable one of the mods until a patch is provided.");
                }
            }
        }
        else{
            throw new NullPointerException("Null values are not permitted in this map!");
        }
    }
}
