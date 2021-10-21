package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.client.render.ICCModelConsumer;
import com.vicious.viciouscore.common.item.ItemEnergoRifle;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Unnecessary item registrator, used during testing.
 */
public class VItemRegistry extends Registrator{
    private static List<Item> itemList = new ArrayList<>();
    private static ItemEnergoRifle ENERGO_RIFLE;
    public static void preInit(){
        ENERGO_RIFLE = register(new ItemEnergoRifle());
    }
    public static <T extends Item>  T register(T in){
        itemList.add(in);
        return in;
    }
    public static void register(RegistryEvent.Register<Item> ev){
        for(Item i : itemList){
            if(i instanceof ICCModelConsumer){

            }
        }
    }
}
