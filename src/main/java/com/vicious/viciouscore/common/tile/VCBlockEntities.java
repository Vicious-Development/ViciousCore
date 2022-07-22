package com.vicious.viciouscore.common.tile;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class VCBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ViciousCore.MODID);
    public static final Map<Class<? extends ViciousTE>,RegistryObject<?>> tiles = new HashMap<>();
    public static RegistryObject<BlockEntityType<TileMultiBlockComponent>> MULTIBLOCKCOMPONENT = register("multiblockcomponent",TileMultiBlockComponent.class,TileMultiBlockComponent::new);
    public static <T extends ViciousTE> RegistryObject<BlockEntityType<T>> register(String tileID, Class<T> cls, BlockEntityType.BlockEntitySupplier<T> constructor, Block... associatedBlocks){
        BlockEntityType<T> bet = BlockEntityType.Builder.of(constructor,associatedBlocks).build(null);
        RegistryObject<BlockEntityType<T>> ro = BER.register(ViciousCore.MODID + "tile" + tileID,()->bet);
        tiles.put(cls,ro);
        return ro;
    }
    public static void init(){}
}
