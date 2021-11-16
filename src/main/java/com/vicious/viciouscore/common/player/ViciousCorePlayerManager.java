package com.vicious.viciouscore.common.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViciousCorePlayerManager {
    public static Map<UUID, ViciousCorePlayerInfo> pinfo = new HashMap<>();
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent ev){
        pinfo.put(ev.player.getUniqueID(), new ViciousCorePlayerInfo());
    }
    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent ev){
        pinfo.remove(ev.player.getUniqueID());
    }
    public static ViciousCorePlayerInfo getPInfo(UUID uuid){
        return pinfo.get(uuid);
    }
}
