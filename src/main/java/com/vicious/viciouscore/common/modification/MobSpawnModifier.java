package com.vicious.viciouscore.common.modification;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.registries.VItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Used to modify mobs on spawn. To do so you can add to the entityModificator map a new handler.
 */
public class MobSpawnModifier {
    private static List<EntityMob> queuedSpawns = new ArrayList<>();
    public static Map<Class<? extends EntityMob>, List<Consumer<EntityMob>>> entityModificators = new HashMap<>();
    public static double weaponReplaceChance = 0.25F;
    //Set this to true to prevent other mods from modifying the entity.
    public static boolean cancelOtherOverrides = false;
    //Override this to customize
    public static List<Consumer<EntityMob>> bipedOverriderList = Lists.newArrayList((EntityMob e)->{
        double rand = Math.random();
        if(rand > weaponReplaceChance) {
            try {
                e.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(VItemRegistry.ENERGO_RIFLE, 1));
                MobSpawnModifier.cancelOtherOverrides = true;
            } catch(NullPointerException ignored){ }
        }
    });
    static{
        entityModificators.put(EntityZombie.class,bipedOverriderList);
        entityModificators.put(EntityZombieVillager.class,bipedOverriderList);
        entityModificators.put(EntitySkeleton.class,bipedOverriderList);
        entityModificators.put(EntityWitherSkeleton.class,bipedOverriderList);
        entityModificators.put(EntityPigZombie.class,bipedOverriderList);
        entityModificators.put(EntityVex.class,bipedOverriderList);
    }
    @SubscribeEvent
    public static void onMobSpawn(EntityJoinWorldEvent ev){
        //Queue only if supported.
        Entity entity = ev.getEntity();
        if(entityModificators.containsKey(entity.getClass())) {
            if(entityModificators.get(entity.getClass()).size() > 0) {
                NBTTagCompound tag = entity.getEntityData();
                if(!tag.hasKey("vfirstjoin")) {
                    tag.setTag("vfirstjoin", new NBTTagInt(1));
                    queuedSpawns.add((EntityMob) entity);
                }
            }
        }
    }
    @SubscribeEvent
    public static void tick(TickEvent event){
        if(event.type != TickEvent.Type.SERVER) return;
        int i = 0;
        while (queuedSpawns.size() > 0 && i < queuedSpawns.size()) {
            EntityMob entity = queuedSpawns.get(i);
            if(entity.ticksExisted <= 0) {
                i++;
                continue;
            }
            else {
                queuedSpawns.remove(i);
            }
            //lol funny name
            List<Consumer<EntityMob>> mobEater = entityModificators.get(entity.getClass());
            if (mobEater != null) {
                for (Consumer<EntityMob> entityMobConsumer : mobEater) {
                    if (cancelOtherOverrides) {
                        cancelOtherOverrides = false;
                        return;
                    }
                    entityMobConsumer.accept(entity);
                }
            }
        }
    }
}
