package com.vicious.viciouscore.common.util;

import net.minecraftforge.common.util.LazyOptional;

public class FuckLazyOptionals {
    public static boolean isPresent(LazyOptional<?> opt){
        return opt.isPresent() && opt.resolve().isPresent();
    }
    public static <T> T getOrNull(LazyOptional<T> opt){
        return isPresent(opt) ? get(opt) : null;
    }

    private static <T> T get(LazyOptional<T> opt) {
        return opt.resolve().get();
    }
}
