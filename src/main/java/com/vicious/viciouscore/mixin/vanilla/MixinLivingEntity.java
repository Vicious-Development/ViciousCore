package com.vicious.viciouscore.mixin.vanilla;

import com.vicious.viciouscore.common.events.TotemUsedEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    /**
     * @reason provide totem death event.
     * @author drathonix
     */
    @Overwrite
    private boolean checkTotemDeathProtection(DamageSource p_21263_) {
        if (p_21263_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            ItemStack ref = null;
            LivingEntity self = asLiving();
            for(InteractionHand interactionhand : InteractionHand.values()) {
                ItemStack itemstack1 = self.getItemInHand(interactionhand);
                if (itemstack1.is(Items.TOTEM_OF_UNDYING)) {
                    ref = itemstack1;
                    break;
                }
            }

            if (ref != null) {
                TotemUsedEvent event = new TotemUsedEvent.Pre(self, ref);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    if (self instanceof ServerPlayer serverplayer) {
                        serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                        CriteriaTriggers.USED_TOTEM.trigger(serverplayer, ref.copy());
                    }
                    ref.shrink(1);
                    self.setHealth(1.0F);
                    self.removeAllEffects();
                    self.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                    self.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                    self.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                    self.level().broadcastEntityEvent(self, (byte) 35);
                    MinecraftForge.EVENT_BUS.post(new TotemUsedEvent.Post(self,ref));
                    return true;
                }
            }
            return false;
        }
    }
    private LivingEntity asLiving(){
        try{
            return LivingEntity.class.cast(this);
        }
        catch(ClassCastException ignored){
            return null;
        }
    }
}
