package com.vicious.viciouscore.common.override;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockOverrider {

    private static Map<Class<?>, Block> overriders = new HashMap<>();
    private static Map<String, BOverrider> queued = new HashMap<>();
    private static Map<Block, Supplier<TileEntity>> tileInjectors = new HashMap<>();

    @SubscribeEvent
    @SuppressWarnings({"unchecked","rawtypes"})
    public static void onBlockEvent(BlockEvent ev) {
        //Don't care
        if(ev instanceof BlockEvent.BreakEvent || ev.isCanceled()) return;
        IBlockState state = ev.getState();
        if(ev instanceof BlockEvent.PlaceEvent) {
            if (tileInjectors.containsKey(state.getBlock())) {
                TileEntity te = tileInjectors.get(state.getBlock()).get();
                if (ev.getWorld().getTileEntity(ev.getPos()) == null) {
                    ev.getWorld().setTileEntity(ev.getPos(), te);
                }
            }
        }
        if(overriders.containsKey(state.getBlock().getClass())){
            Block overridden = overriders.get(state.getBlock().getClass());
            BlockStateContainer.Builder builder =  new BlockStateContainer.Builder(overridden);
            IBlockState result = builder.build().getBaseState();
            Collection<IProperty<?>> keys = state.getPropertyKeys();
            for (IProperty key : keys) {
                result = result.withProperty(key,state.getValue(key));
            }
            ev.getWorld().setBlockState(ev.getPos(),result);
        }
    }

    public static void init(){
        queued.forEach((clazz,overrider)->{
            if(Loader.isModLoaded(overrider.modid)){
                try {
                    Class<?> cls =  Class.forName(clazz);
                    overriders.put(cls,overrider.block);
                    ViciousCore.logger.info("Registered a block overrider for: " + cls);
                } catch(ClassNotFoundException e){
                    ViciousCore.logger.error("Did not register a block overider " + clazz + " but expected to as the target mod was loaded.");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Registers a tile entity for a block. This will create a tileEntity when the target block is placed.
     */
    public static void registerTileEntity(Block b, Supplier<TileEntity> te){
        tileInjectors.put(b,te);
        ViciousCore.logger.info("Registered a Tile Entity Injector for Block " + b.getRegistryName());
    }
    public static void registerOverrider(String targetBlockClassCanonicalName, Block blockTypeToConvertTo, String targetModid){
        queued.putIfAbsent(targetBlockClassCanonicalName,new BOverrider(targetModid,blockTypeToConvertTo));
    }
    private static class BOverrider {
        private String modid;
        private Block block;
        public BOverrider(String modid, Block block){
            this.modid=modid;
            this.block =block;
        }
    }
}
