package com.vicious.viciouscore.common.modification;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.registries.VItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Used to modify mobs on spawn. To do so you can add to the entityModificator map a new handler.
 */
public class MobSpawnModifier {
    public static Map<Class<? extends EntityMob>, List<Consumer<EntityMob>>> entityModificators = new HashMap<>();
    public static float weaponReplaceChance = 0F;
    //Set this to true to prevent other mods from modifying the entity.
    public static boolean cancelOtherOverrides = false;
    //Override this to customize
    public static List<Consumer<EntityMob>> bipedOverriderList = Lists.newArrayList((EntityMob e)->{
        double rand = Math.random();
        if(rand > weaponReplaceChance) {
            e.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(VItemRegistry.ENERGO_RIFLE, 1));
            MobSpawnModifier.cancelOtherOverrides = true;
        }
    });
    static{
        entityModificators.put(EntityZombie.class,bipedOverriderList);
        entityModificators.put(EntityZombieVillager.class,bipedOverriderList);
        entityModificators.put(EntitySkeleton.class,bipedOverriderList);
        entityModificators.put(EntityPigZombie.class,bipedOverriderList);
    }
    @SubscribeEvent
    public static void onMobSpawn(EntityEvent.EntityConstructing ev){
        Entity ent = ev.getEntity();
        //lol funny name
        List<Consumer<EntityMob>> mobEater = entityModificators.get(ent.getClass());
        if(mobEater != null){
            for (Consumer<EntityMob> entityMobConsumer : mobEater) {
                if(cancelOtherOverrides){
                    cancelOtherOverrides = false;
                    return;
                }
                entityMobConsumer.accept((EntityMob) ent);
            }
        }
    }
}
