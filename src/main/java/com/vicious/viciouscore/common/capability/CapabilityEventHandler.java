package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.interfaces.ICapabilityDeathPersistant;
import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.capability.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableGlobalData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableLevelData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncablePlayerData;
import com.vicious.viciouscore.common.resource.VCResources;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;


public class CapabilityEventHandler {

    @SubscribeEvent
    public static void onAttachEntity(AttachCapabilitiesEvent<Entity> event){
        Entity e = event.getObject();
        if(e instanceof Player p){
            attach(event, VCResources.COMMONKEYCAPABILITY, VCCapabilities.KEYPRESS,KeyPressHandler::new,e);
            event.addCapability(VCResources.PLAYERDATA,new SyncablePlayerData(p));
        }
    }
    @SubscribeEvent
    public static void onAttachLevel(AttachCapabilitiesEvent<Level> event){
        Level l = event.getObject();
        if(l instanceof ServerLevel sl) {
            event.addCapability(VCResources.LEVELDATA, new SyncableLevelData(l));
            event.addCapability(VCResources.GLOBALDATA, SyncableGlobalData.getInstance());
        }
    }
    public static <T extends IVCCapabilityHandler> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<T> token, NonNullSupplier<T> capSupplier, Object target){
        event.addCapability(key, new AttachableCapabilityProvider<>(LazyOptional.of(capSupplier),token,target));
    }

    private static final Map<UUID, List<ICapabilityDeathPersistant>> toTransfer = new HashMap<>();
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDeath(LivingDeathEvent event){
        if(event.getEntity() instanceof ServerPlayer sp){
            List<ICapabilityDeathPersistant> toPersist = new ArrayList<>();
            for (Capability<? extends IVCCapabilityHandler> token : VCCapabilities.tokens()) {
                IVCCapabilityHandler cap = FuckLazyOptionals.getOrNull(sp.getCapability(token));
                if(cap instanceof ICapabilityDeathPersistant cdp){
                    toPersist.add(cdp);
                }
            }
            if(toTransfer.putIfAbsent(sp.getUUID(),toPersist) != null){
                toTransfer.replace(sp.getUUID(),toPersist);
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event) {
        if(event.isWasDeath()){
            Player original = event.getOriginal();
            if(original instanceof ServerPlayer) {
                Player copy = event.getEntity();
                List<ICapabilityDeathPersistant> caps = toTransfer.get(copy.getUUID());
                for (ICapabilityDeathPersistant cap : caps) {
                    cap.copyTo(copy);
                }
                toTransfer.remove(copy.getUUID());
            }
        }
    }
}
