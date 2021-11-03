package com.vicious.viciouscore.client.render.entity.model;

import com.vicious.viciouscore.client.configuration.EntityModelOverrideCFG;
import com.vicious.viciouscore.client.configuration.EntityPartTransformOverrideCFG;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.EnumHandSide;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface IOverrideModel {
    void applicate(EntityModelOverrideCFG<? extends ModelBase> configurations);
    void queueTransformer(Runnable in);
    void ignoreHandSide(EnumHandSide in);
    default void applicatePart(ModelRenderer part, EntityPartTransformOverrideCFG config){
        if(config.overrideRotation.getBoolean()){
            part.rotateAngleX= (float) Math.toRadians(config.rx.value());
            part.rotateAngleY= (float) Math.toRadians(config.ry.value());
            part.rotateAngleZ= (float) Math.toRadians(config.rz.value());
        }
        if(config.overrideTranslation.getBoolean()){
            part.offsetX=config.tx.value();
            part.offsetY=config.ty.value();
            part.offsetZ=config.tz.value();
        }
    }
    default void applicate(Map<String,Field> partMap, EntityModelOverrideCFG<?> configurations){
        if(configurations == null) return;
        partMap.forEach((name,field)->{
            EntityPartTransformOverrideCFG cfg = null;
            if(!field.getType().isArray()) {
                cfg = configurations.getPartConfiguration(name);
            } else{
                ModelRenderer[] arr = (ModelRenderer[]) Reflection.accessField(field,this);
                for (int i = 0; i < arr.length; i++) {
                    cfg = configurations.getPartConfiguration(name+i);
                    if(cfg == null) continue;
                    if(cfg.active.getBoolean()) applicatePart(arr[i],cfg);
                }
            }
            if(cfg == null) return;
            if(cfg.active.getBoolean()) applicatePart((ModelRenderer) Reflection.accessField(field,this),cfg);
        });
    }

    /**
     * Some vanilla models don't get their transforms reset by default. The result is that models retain rotations when they shouldn't
     * This fixes that.
     * @param parts
     */
    default void resetTransformations(List<ModelRenderer> parts){
        for (ModelRenderer part : parts) {
            part.rotateAngleX = 0;
            part.rotateAngleY = 0;
            part.rotateAngleZ = 0;
            part.offsetX=0;
            part.offsetY=0;
            part.offsetZ=0;
        }
    }

    default void clone(ModelBase og, Map<String,Field> partMap){
        Class<?> clazz = og.getClass();
        while(clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                Field f2 = Reflection.getField(this, f.getName());
                try {
                    f2.setAccessible(true);
                    f2.set(this, Reflection.accessField(f, og));
                } catch (IllegalAccessException ignored) {}
                if (f.getType() == ModelRenderer.class) {
                    partMap.put(f2.getName(), f2);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
