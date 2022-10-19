package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;

import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouslib.aunotamation.annotation.AllowedIn;
import com.vicious.viciouslib.aunotamation.annotation.ModifiedWith;
import com.vicious.viciouslib.aunotamation.annotation.NotModifiedWith;
import com.vicious.viciouslib.aunotamation.annotation.RequiredType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * The overarching annotation for SyncableValue automation.
 *
 * Annotations extending this one can only be using in ISyncableCompoundHolder implementers and only on public SyncableValues
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@AllowedIn(ISyncableCompoundHolder.class)
@RequiredType(SyncableValue.class)
@ModifiedWith({Modifier.PUBLIC})
@NotModifiedWith({Modifier.STATIC,Modifier.FINAL})
public @interface SyncableAnnotation {
}
