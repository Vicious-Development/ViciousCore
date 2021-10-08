package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.entity.projectile.OrbProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;


public class VEntityRegistry extends Registrator{
    public static void register(){
        registerEntity("orbprojectile", OrbProjectile.class, nextIntID(), 50, 16777215, 0);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int colour1, int colour2) {
        EntityRegistry.registerModEntity(
                new ResourceLocation(
                        ViciousCore.MODID + ":" + name.replaceAll(" ","").toLowerCase()
                ), entity, name, id, ViciousCore.instance, range, 1, true, colour1, colour2);
    }
}
