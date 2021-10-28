package com.vicious.viciouscore.common.modification;

import com.vicious.viciouscore.common.registries.VItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Used to modify mobs on spawn. To do so you can add to the entityModificator map a new handler.
 */
public class MobSpawnModifier {
    public static Map<Class<? extends EntityMob>, Consumer<EntityMob>> entityModificators = new HashMap<>();
    public static float weaponReplaceChance = 0F;
    //Override this to customize
    public static Consumer<EntityMob> bipedOverrider = (EntityMob e)->{
        double rand = Math.random();
        if(rand > weaponReplaceChance) {
            e.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(VItemRegistry.ENERGO_RIFLE, 1));
        }
    };
    static{
        entityModificators.put(EntityZombie.class,bipedOverrider);
        entityModificators.put(EntityZombieVillager.class,bipedOverrider);
        entityModificators.put(EntitySkeleton.class,bipedOverrider);
        entityModificators.put(EntityPigZombie.class,bipedOverrider);
    }
    @SubscribeEvent
    public static void onMobSpawn(EntityEvent.EntityConstructing ev){
        Entity ent = ev.getEntity();
        //lol funny name
        Consumer<EntityMob> mobEater = entityModificators.get(ent.getClass());
        if(mobEater != null){
            mobEater.accept((EntityMob) ent);
        }
    }
}
