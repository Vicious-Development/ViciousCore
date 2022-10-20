package com.vicious.viciouscore.aunotamation.registry;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class RegistryAutomator {
    public static final RegistryAnnotationProcessor UNIVERSALREGISTRYPROCESSOR = new RegistryAnnotationProcessor();
    private static boolean init = false;
    public static void init() {
        if (init) return;
        init = true;
        UNIVERSALREGISTRYPROCESSOR.addSupport(new RegistryProcessor<>(BlockEntity.class, ForgeRegistries.BLOCK_ENTITIES,ForgeRegistries.BLOCKS, BlockPos.class, BlockState.class) {
            @Override
            public BlockEntityType<?> supply(Class<BlockEntity> targetClass, Constructor<BlockEntity> constructor, List<Block> associations, Field targetField) {
                //Create the block entity type.
                return BlockEntityType.Builder.of((pos, state) -> {
                    try {
                        BlockEntity be = constructor.newInstance(pos, state);
                        Aunotamation.processObject(be);
                        return be;
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e){
                        ViciousCore.logger.fatal("Failed to create a block entity. Caused by internal exception: " + e.getCause());
                        e.printStackTrace();
                    }
                    return null;
                }, associations.toArray(new Block[0])).build(null);
            }
        });
        UNIVERSALREGISTRYPROCESSOR.addSupport(new RegistryProcessor(AbstractContainerMenu.class,ForgeRegistries.CONTAINERS,null,int.class, Inventory.class, FriendlyByteBuf.class) {
            @Override
            public MenuType<?> supply(Class targetClass, Constructor constructor, List associations, Field targetField) {
                return IForgeMenuType.create((num,plrinv,buf)->{
                    try {
                        AbstractContainerMenu menu = (AbstractContainerMenu) constructor.newInstance(num,plrinv,buf);
                        Aunotamation.processObject(menu);
                        return menu;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        ViciousCore.logger.fatal("Failed to create a menu. Caused by internal exception: " + e.getCause());
                        e.printStackTrace();
                    }
                    return null;
                });
            }
        });
        Aunotamation.registerProcessor(UNIVERSALREGISTRYPROCESSOR);
    }
}
