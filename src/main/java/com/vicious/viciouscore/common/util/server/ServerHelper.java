package com.vicious.viciouscore.common.util.server;

import com.vicious.viciouscore.common.phantom.WorldPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        if(level == null) return "---null---";
        ServerLevelData dat = (ServerLevelData) level.getLevelData();
        return dat.getLevelName();
    }

    public static ServerLevel getMainLevel() {
        return server.getLevel(Level.OVERWORLD);
    }
    public static List<ServerPlayer> getPlayers(){
        return server.getPlayerList().getPlayers();
    }

    public static void forAllPlayersExcept(Player except, Consumer<ServerPlayer> cons){
        for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
            if(!onlinePlayer.equals(except)) cons.accept(onlinePlayer);
        }
    }
    public static void forAllPlayers(Consumer<ServerPlayer> cons){
        server.getPlayerList().getPlayers().forEach(cons);
    }

    public static WorldPos getDefaultRespawnPos(){
        ServerLevel l = getMainLevel();
        return new WorldPos(l,l.getSharedSpawnPos());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerAboutToStartEvent event){
        server = event.getServer();
    }

}
