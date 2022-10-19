package com.vicious.viciouscore.aunotamation.commonkeybinding;


import com.vicious.viciouscore.common.keybinding.CommonKeyBinding;
import com.vicious.viciouslib.aunotamation.annotation.ModifiedWith;
import com.vicious.viciouslib.aunotamation.annotation.RequiredType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RequiredType(CommonKeyBinding.class)
@ModifiedWith({Modifier.PUBLIC})
public @interface Key {
}
