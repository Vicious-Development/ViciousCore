package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.interfaces.ICapabilityDeathPersistant;
import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.capability.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.data.implementations.SyncableInventory;
import com.vicious.viciouscore.common.data.implementations.SyncableRecipeState;
import com.vicious.viciouscore.common.data.implementations.SyncableTickState;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.data.structures.SyncableIVCNBT;
import com.vicious.viciouscore.common.data.structures.SyncablePrimitive;
import com.vicious.viciouscore.common.resource.VCResources;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VCCapabilities {
    private static Map<Class<? extends IVCCapabilityHandler>, Capability<? extends IVCCapabilityHandler>> capabilityTokens = new HashMap<>();
    public static final Capability<KeyPressHandler> KEYPRESS = addToken(KeyPressHandler.class);
    public static final Capability<SyncableTickState> TICKABLE = addToken(SyncableTickState.class);
    public static final Capability<SyncableInventory> FASTITEMSTACKHANDLER = addToken(SyncableInventory.class);
    public static final Capability<SyncableRecipeState> RECIPEPROCESSOR = addToken(SyncableRecipeState.class);
    public static final Capability<SyncablePrimitive> PRIMITIVE = addToken(SyncablePrimitive.class);
    public static final Capability<SyncableINBTCompound> INBT = addToken(SyncableINBTCompound.class);
    public static final Capability<SyncableCompound> COMPOUND = addToken(SyncableCompound.class);
    public static final Capability<SyncableIVCNBT> IVCNBT = addToken(SyncableIVCNBT.class);

    public static <T extends IVCCapabilityHandler> T getCapability(ICapabilityProvider provider, Class<T> cls) {
        return FuckLazyOptionals.getOrNull(provider.getCapability(getToken(cls)));
    }

    @SubscribeEvent
    public void onCapRegistry(RegisterCapabilitiesEvent event) {
        for (Class<? extends IVCCapabilityHandler> CHC : capabilityTokens.keySet()) {
            event.register(CHC);
        }
    }

    public static Collection<Capability<? extends IVCCapabilityHandler>> tokens(){
        return capabilityTokens.values();
    }

    @SuppressWarnings("unchecked")
    public static  <T extends IVCCapabilityHandler> Capability<T> getToken(Class<T> capabilityClass) {
        return (Capability<T>) capabilityTokens.get(capabilityClass);
    }

    public static <T extends IVCCapabilityHandler> Capability<T> addToken(Class<T> cls) {
        Capability<T> capToken = CapabilityManager.get(new CapabilityToken<>() {
        });
        capabilityTokens.put(cls, capToken);
        return capToken;
    }

    @SubscribeEvent
    public static void onAttachEntity(AttachCapabilitiesEvent<Entity> event){
        Entity e = event.getObject();
        if(e instanceof Player){
            attach(event, VCResources.COMMONKEYCAPABILITY,KeyPressHandler.class,KeyPressHandler::new,e);
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
