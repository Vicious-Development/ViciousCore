package com.vicious.viciouscore.common.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class TotemUsedEvent extends LivingEvent {
    private final ItemStack totem;
    public TotemUsedEvent(LivingEntity entity, ItemStack totem) {
        super(entity);
        this.totem=totem;
    }
    public ItemStack getTotem(){
        return totem;
    }

    public static class Pre extends TotemUsedEvent{
        public Pre(LivingEntity entity, ItemStack totem) {
            super(entity, totem);
        }
    }
    public static class Post extends TotemUsedEvent{
        public Post(LivingEntity entity, ItemStack totem) {
            super(entity, totem);
        }
    }
}
