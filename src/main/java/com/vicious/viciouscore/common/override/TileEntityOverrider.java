package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TileEntityOverrider {
    private static Map<Class<?>, Function<TileEntity,TileEntity>> overriders = new HashMap<>();
    private static Map<String, TEOverrider> queued = new HashMap<>();
    private static Map<World, Integer> prevTileCounts = new HashMap<>();

    /**
     * I tried doing this in an easier way by accessing addedTileEntityList which by ALL MEANS should have worked.
     * It didn't. For some reason, this code works correctly.
     */
    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END || tick.side == Side.CLIENT) return;
        World world = tick.world;
        List<TileEntity> loadedTileEntityList = (List<TileEntity>) Reflection.accessField(world, "loadedTileEntityList");
        prevTileCounts.putIfAbsent(world,loadedTileEntityList.size());
        if (prevTileCounts.get(world) != loadedTileEntityList.size()) {
            prevTileCounts.replace(world, loadedTileEntityList.size());
            for (TileEntity ent : loadedTileEntityList) {
                if (overriders.containsKey(ent.getClass())) {
                    System.out.println("sifted" + ent.getClass());
                    TileEntity overridden = overriders.get(ent.getClass()).apply(ent);
                    overridden.setWorld(world);
                    world.setTileEntity(ent.getPos(), overridden);
                }
            }
        }
    }
    public static void init(){
        queued.forEach((clazz,teo)->{
            if(Loader.isModLoaded(teo.modid)){
                try {
                    Class<?> tileClass =  Class.forName(clazz);
                    overriders.put(tileClass,teo.func);
                    System.out.println("REGISTERED TILE ENTITY OVERRIDE FOR: "+ tileClass);
                } catch(ClassNotFoundException ignored){
                    System.out.println("Did not register a tile entity overrider.");
                    ignored.printStackTrace();
                }
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
