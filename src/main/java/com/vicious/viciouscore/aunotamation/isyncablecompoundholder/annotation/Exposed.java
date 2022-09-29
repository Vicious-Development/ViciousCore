package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;


import com.vicious.viciouslib.aunotamation.all.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Effect: Adds the field's value to an ExposableSyncableCompound and then adds that to the holder's compound.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Extends(SyncableAnnotation.class)
public @interface Exposed {
}
