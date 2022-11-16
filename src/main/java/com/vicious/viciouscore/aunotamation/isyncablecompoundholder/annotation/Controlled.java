package com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation;


import com.vicious.viciouslib.aunotamation.annotation.Extends;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Same effect as read only but is intended to indicate that the Field is modified on the server via custom packets.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Extends(ReadOnly.class)
public @interface Controlled {
}
