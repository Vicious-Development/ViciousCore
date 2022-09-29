package com.vicious.viciouscore.aunotamation.registry.annotation;

import com.vicious.viciouslib.aunotamation.all.annotation.ModifiedWith;
import com.vicious.viciouslib.aunotamation.all.annotation.NotModifiedWith;
import com.vicious.viciouslib.aunotamation.all.annotation.RequiredType;
import net.minecraftforge.registries.RegistryObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

@ModifiedWith({Modifier.STATIC, Modifier.PUBLIC})
@NotModifiedWith(Modifier.FINAL)
@RequiredType(RegistryObject.class)
public @interface RegistryAnnotation {

}
