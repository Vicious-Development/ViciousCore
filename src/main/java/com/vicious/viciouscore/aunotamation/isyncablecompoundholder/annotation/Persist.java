package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;

import com.vicious.viciouslib.aunotamation.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Effect: Automatically adds the field to the ISyncableCompoundHolder's compound.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Extends(SyncableAnnotation.class)
public @interface Persist {
}
