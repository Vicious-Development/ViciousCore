package com.vicious.viciouscore.common.util;

public class Randomizer {
    public static <T> T randomOfOptions(T... options){
        return options[(int) (Math.random()*options.length)];
    }
}
