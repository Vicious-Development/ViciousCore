package com.vicious.viciouscore.aunotamation.registry.annotation;

import com.vicious.viciouslib.aunotamation.annotation.ModifiedWith;
import com.vicious.viciouslib.aunotamation.annotation.RequiredType;
import net.minecraftforge.registries.RegistryObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

/**
 * Marks the DeferredRegister in a registry class.
 * There can only be one!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

@ModifiedWith({Modifier.STATIC, Modifier.PUBLIC})
@RequiredType(RegistryObject.class)
public @interface Registry {
}
