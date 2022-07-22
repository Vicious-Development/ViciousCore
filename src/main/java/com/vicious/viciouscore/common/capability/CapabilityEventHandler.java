package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.interfaces.ICapabilityDeathPersistant;
import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.capability.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.resource.VCResources;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEventHandler {
    @SubscribeEvent
    public static void onAttachEntity(AttachCapabilitiesEvent<Entity> event){
        Entity e = event.getObject();
        if(e instanceof Player){
            attach(event,VCResources.COMMONKEYCAPABILITY,KeyPressHandler.class,KeyPressHandler::new,e);
        }
    }
    public static <T extends IVCCapabilityHandler> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Class<T> capClass, NonNullSupplier<T> capSupplier, Object target){
        event.addCapability(key, new AttachableCapabilityProvider<>(LazyOptional.of(capSupplier),VCCapabilities.getToken(capClass),target));
    }
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        if(event.isWasDeath()){
            Player original = event.getOriginal();
            Player copy = event.getEntity();
            for (Capability<? extends IVCCapabilityHandler> token : VCCapabilities.tokens()) {
                IVCCapabilityHandler handler = FuckLazyOptionals.getOrNull(original.getCapability(token));
                IVCCapabilityHandler copyHandler = FuckLazyOptionals.getOrNull(copy.getCapability(token));
                if(handler instanceof ICapabilityDeathPersistant && copyHandler instanceof ICapabilityDeathPersistant){
                    ((ICapabilityDeathPersistant) handler).copyTo((ICapabilityDeathPersistant) copyHandler);
                }
            }
        }
    }
}
