package com.vicious.viciouscore.aunotamation;

import java.util.HashMap;
import java.util.Map;

/**
 * A list of exposure types that are forced to be accessible.
 */
public class ForcedExposure {
    private static final Map<String,Object> m = new HashMap<>();
    public static final ForcedExposure PLAYERS = addType("PLAYERS",new ForcedExposure.PLAYERS());
    public static Object getExposureOfType(String type){
        return m.get(type);
    }
    public static ForcedExposure addType(String key, ForcedExposure type){
        m.put(key,type);
        return type;
    }

    private static class PLAYERS extends ForcedExposure{}
}
