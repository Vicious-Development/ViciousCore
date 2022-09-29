package com.vicious.viciouscore.common.capability;

import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.capability.types.keypresshandler.KeyPressHandler;
import com.vicious.viciouscore.common.data.implementations.SyncableInventory;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableGlobalData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableLevelData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncablePlayerData;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import net.minecraftforge.common.capabilities.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VCCapabilities {
    public static Map<Class<? extends IVCCapabilityHandler>, Capability<? extends IVCCapabilityHandler>> capabilityTokens = new HashMap<>();
    public static Capability<KeyPressHandler> KEYPRESS;
    public static Capability<SyncableInventory> FASTITEMSTACKHANDLER;
    public static Capability<SyncablePlayerData> PLAYERDATA;
    public static Capability<SyncableGlobalData> GLOBALDATA;
    public static Capability<SyncableLevelData> LEVELDATA;
    public static <T extends IVCCapabilityHandler> T getCapability(ICapabilityProvider provider, Class<T> cls) {
        return FuckLazyOptionals.getOrNull(provider.getCapability(getToken(cls)));
    }

    public static Collection<Capability<? extends IVCCapabilityHandler>> tokens(){
        return capabilityTokens.values();
    }

    @SuppressWarnings("unchecked")
    public static  <T extends IVCCapabilityHandler> Capability<T> getToken(Class<T> capabilityClass) {
        return (Capability<T>) capabilityTokens.get(capabilityClass);
    }
    /**
     * The way forge handles capabilities is the DUMBEST SHIT OF ALL TIME. Let me explain.
     * Forge does this thing with ASM where it gets the Capability object's generic type. If you provide a generic type of anything that isn't a classname
     * it fails.
     *
     * Forge. Forge. Please. Just make the token a Class object as tokens. With this you don't need to use ASM.
     * All you need to do is provide the Class you want a capability to extend, for example with
     * CapabilityItemHandler, remove the capability, just use IItemHandler.class as the token. Or hell if you're really this fucking insane
     * Make a CapabilityToken class and just have the capability key be bound to the token.
     * Fuck you forge, wasting hours of my time with stupid shit like debugging your poorly explained garbage.
     *
    public static <T extends IVCCapabilityHandler> Capability<T> addToken(Class<T> cls, RegisterCapabilitiesEvent event) {
        event.register(cls);
        Capability<T> capToken = CapabilityManager.get(new CapabilityToken<>() {
        });
        capabilityTokens.put(cls, capToken);
        return capToken;
    }*/

    public static void onCapRegistry(RegisterCapabilitiesEvent event) {
        //Can't be simplified see the rant I went on above.
        event.register(KeyPressHandler.class);
        KEYPRESS = CapabilityManager.get(new CapabilityToken<>(){});
        capabilityTokens.put(KeyPressHandler.class,KEYPRESS);
        event.register(SyncableInventory.class);
        FASTITEMSTACKHANDLER = CapabilityManager.get(new CapabilityToken<>(){});
        capabilityTokens.put(SyncableInventory.class,FASTITEMSTACKHANDLER);
        event.register(SyncablePlayerData.class);
        PLAYERDATA = CapabilityManager.get(new CapabilityToken<>(){});
        capabilityTokens.put(SyncablePlayerData.class,PLAYERDATA);
        event.register(SyncableGlobalData.class);
        GLOBALDATA = CapabilityManager.get(new CapabilityToken<>(){});
        capabilityTokens.put(SyncableGlobalData.class,GLOBALDATA);
        event.register(SyncableLevelData.class);
        LEVELDATA = CapabilityManager.get(new CapabilityToken<>(){});
        capabilityTokens.put(SyncableLevelData.class,LEVELDATA);
    }
}
