package com.vicious.viciouscore.common.configuration;

import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TileEntityOverrider {
    private static Map<Class<? extends TileEntity>, Function<TileEntity,TileEntity>> overriders = new HashMap<>();
    private static Map<String, TEOverrider> queued = new HashMap<>();

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent tick) {
        if (tick.phase != TickEvent.Phase.START) return;
        for (WorldServer world : DimensionManager.getWorlds()) {
            List<TileEntity> addedTileEntityList = (List<TileEntity>) Reflection.accessField(world, "addedTileEntityList");
            for (int i = 0; i < addedTileEntityList.size(); i++) {
                TileEntity ent = addedTileEntityList.get(i);
                if (overriders.containsKey(ent.getClass())) {
                    addedTileEntityList.set(i, overriders.get(ent.getClass()).apply(ent));
                }
            }
        }
    }
    public static void init(){
        queued.forEach((clazz,teo)->{
            if(Loader.isModLoaded(teo.modid)){
                try {
                    Class<?> tileClass =  Class.forName(clazz);

                } catch(ClassNotFoundException ignored){}
            }
        });
    }
    public static void registerOverrider(String targetTileClassCanonicalName, Function<TileEntity,TileEntity> converterFunction, String targetModid){
        queued.putIfAbsent(targetTileClassCanonicalName,new TEOverrider(targetModid,converterFunction));
    }
    private static class TEOverrider{
        private String modid;
        private Function<TileEntity,TileEntity> func;
        public TEOverrider(String modid, Function<TileEntity,TileEntity> func){
            this.modid=modid;
            this.func=func;
        }
    }
}
