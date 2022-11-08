package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.interfaces.ICapabilityDeathPersistant;
import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.capability.types.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableLevelData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncablePlayerData;
import com.vicious.viciouscore.common.resource.VCResources;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
        if(e instanceof Player p){
            attach(event, VCResources.COMMONKEYCAPABILITY, VCCapabilities.KEYPRESS,KeyPressHandler::new,e);
            event.addCapability(VCResources.PLAYERDATA, Aunotamation.processObject(new SyncablePlayerData(p)));
        }
    }
    @SubscribeEvent
    public static void onAttachLevel(AttachCapabilitiesEvent<Level> event){
        Level l = event.getObject();
        if(l instanceof ServerLevel sl) {
            event.addCapability(VCResources.LEVELDATA, Aunotamation.processObject(new SyncableLevelData(l)));
        }
    }
    public static <T extends IVCCapabilityHandler> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<T> token, NonNullSupplier<T> capSupplier, Object target){
        event.addCapability(key, new AttachableCapabilityProvider<>(LazyOptional.of(capSupplier),token,target));
    }
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        original.reviveCaps();
        if(original instanceof ServerPlayer) {
            Player copy = event.getEntity();
            VCCapabilities.capabilityTokens.forEach((k,v)->{
                IVCCapabilityHandler cap = FuckLazyOptionals.getOrNull(original.getCapability(v));
                if(cap instanceof ICapabilityDeathPersistant pers){
                    pers.copyTo(copy);
                }
            });

        }
        original.invalidateCaps();
    }
}
