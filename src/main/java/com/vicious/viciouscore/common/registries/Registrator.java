package com.vicious.viciouscore.common.registries;

public class Registrator {
    private static int nextId = 0;
    public static int nextIntID(){
        return ++nextId;
    }
}
