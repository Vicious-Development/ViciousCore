package com.vicious.viciouscore.common.registries;

public class Registrator {
    private static int nextid = 0;
    public static int nextIntID(){
        return ++nextid;
    }
}
