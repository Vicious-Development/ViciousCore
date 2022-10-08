package com.vicious.viciouscore.aunotamation.registry.annotation;


import com.vicious.viciouslib.aunotamation.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

@Extends(RegistryAnnotation.class)
public @interface LinkBE {
    Class<?> value();
}
