package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.common.item.ItemEnergoRifle;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Unnecessary item registrator, used during testing.
 */
@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class VItemRegistry extends Registrator{
    private static List<Item> itemList = new ArrayList<>();
    public static ItemEnergoRifle ENERGO_RIFLE = register(new ItemEnergoRifle());
    public static <T extends Item> T register(T in){
        itemList.add(in);
        return in;
    }
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> ev){
        System.out.println("ENERG: " + ENERGO_RIFLE);
        IForgeRegistry<Item> reg = ev.getRegistry();
        for(Item i : itemList){
            reg.register(i);
            if(i instanceof IRenderOverride){
                ((IRenderOverride)i).registerRenderers();
            }
        }
    }
}
