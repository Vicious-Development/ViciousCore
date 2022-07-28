package com.vicious.viciouscore.common.util;

@FunctionalInterface
public interface QuadConsumer <W,X,Y,Z>{
    void accept(W w, X x, Y y, Z z);
}
