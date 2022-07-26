package com.vicious.viciouscore.common.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ServerHelper {
    public static MinecraftServer server;
    private static Map<String, ServerLevel> levels = new HashMap<>();
    public static ServerLevel getLevelByName(String name){
        if(!levels.containsKey(name)){
            for (ServerLevel level : server.getAllLevels()) {
                if(getLevelName(level).equals(name)){
                    levels.put(name,level);
                    break;
                }
            }
        }
        return levels.get(name);
    }
    public static String getLevelName(ServerLevel level){
        ServerLevelData dat = (ServerLevelData) level.getLevelData();
        return dat.getLevelName();
    }

    @SubscribeEvent
    public void onServerStarted(ServerAboutToStartEvent event){
        server = event.getServer();
    }
}
