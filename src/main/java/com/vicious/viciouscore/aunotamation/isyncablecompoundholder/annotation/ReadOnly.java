package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;


import com.vicious.viciouslib.aunotamation.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Effect: Sets sendRemote to true and readRemote to false.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Extends(Persist.class)
public @interface ReadOnly {
}
