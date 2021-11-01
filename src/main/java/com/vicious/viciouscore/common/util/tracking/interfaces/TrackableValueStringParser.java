package com.vicious.viciouscore.common.util.tracking.interfaces;

public interface TrackableValueStringParser<T> {
    T parse(String s) throws Exception;
}
