package com.vicious.viciouscore.common.override.chunk;

import com.vicious.viciouscore.ViciousCoreLoadingPlugin;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Only for vicious use. Do not use.
 */
public class ChunkOverrideHandler {
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent ev){
        if(ev.getWorld().isRemote) return;
        override(ev.getChunk());
    }
    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent ev){
        override(ev.getChunkInstance());
    }

    /**
     * Replace the vanilla chunk with a vicious chunk.
     */
    public static void override(Chunk c){
        if(ViciousCoreLoadingPlugin.isSpongeLoaded) return;
        ViciousChunk chunk = new ViciousChunk(c);
        IChunkProvider prov = chunk.getWorld().getChunkProvider();
        if(prov instanceof ChunkProviderServer){
            ChunkProviderServer provider = (ChunkProviderServer) prov;
            if(provider.id2ChunkMap.putIfAbsent(ChunkPos.asLong(chunk.x, chunk.z),chunk) != null) {
                provider.id2ChunkMap.replace(ChunkPos.asLong(chunk.x, chunk.z), chunk);
            }
        }
    }
}
