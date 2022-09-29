package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;


import com.vicious.viciouslib.aunotamation.all.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Effect: Sets sendRemote to true and readRemote to true.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Extends(Persist.class)
public @interface Editable {
}
