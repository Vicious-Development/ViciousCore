package com.vicious.viciouscore.aunotamation.registry;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.aunotamation.registry.annotation.Properties;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

public class RegistryAutomator {
    public static final RegistryAnnotationProcessor UNIVERSALREGISTRYPROCESSOR = new RegistryAnnotationProcessor();
    private static boolean init = false;
    public static void init() {
        if (init) return;
        init = true;
        UNIVERSALREGISTRYPROCESSOR.addSupport(new RegistryProcessor<>(BlockEntity.class, ForgeRegistries.BLOCK_ENTITY_TYPES,ForgeRegistries.BLOCKS, BlockPos.class, BlockState.class) {
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
        UNIVERSALREGISTRYPROCESSOR.addSupport(new RegistryProcessor<>(Item.class,ForgeRegistries.ITEMS,null, Item.Properties.class) {
            @Override
            public Item supply(Class<Item> targetClass, Constructor<Item> constructor, List<Object> associations, Field targetField) throws Exception {
                Properties properties = targetField.getAnnotation(Properties.class);
                if(properties == null){
                    getAnnotationProcessor().err(targetField,"is missing @Properties pointing to the properties field");
                }
                Class<?> regClass = targetField.getDeclaringClass();
                Field f = regClass.getDeclaredField(properties.value());
                if(f.getType() != Item.Properties.class){
                    getAnnotationProcessor().err(f,"must be of type: " + Item.Properties.class.getCanonicalName());
                }
                if(!Modifier.isStatic(f.getModifiers()) || !Modifier.isPublic(f.getModifiers())){
                    getAnnotationProcessor().err(f,"must be public and static");
                }
                return constructor.newInstance(f.get(regClass));
            }
        });
        UNIVERSALREGISTRYPROCESSOR.addSupport(new RegistryProcessor<>(AbstractContainerMenu.class,ForgeRegistries.MENU_TYPES,null,int.class, Inventory.class, FriendlyByteBuf.class) {
            @Override
            public MenuType<?> supply(Class<AbstractContainerMenu> targetClass, Constructor<AbstractContainerMenu> constructor, List<Object> associations, Field targetField) {
                return IForgeMenuType.create((num,plrinv,buf)->{
                    try {
                        AbstractContainerMenu menu = constructor.newInstance(num,plrinv,buf);
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
