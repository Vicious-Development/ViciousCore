package com.vicious.viciouscore.aunotamation.registry.annotation;

import com.vicious.viciouslib.aunotamation.annotation.ModifiedWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * Marks the IForgeRegistry in a registry class.
 * There can only be one!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ModifiedWith({Modifier.STATIC, Modifier.PUBLIC})
public @interface Registry {
    String value();
}
