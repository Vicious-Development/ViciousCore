package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.common.block.ViciousBlock;
import com.vicious.viciouscore.common.sampleblock.SampleBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class VBlockRegistry extends Registrator {
    private static final List<Block> BLOCK_LIST = new ArrayList<>();
    public static final SampleBlock SAMPLE_BLOCK = register(new SampleBlock("sampleblock"));
    public static <T extends Block> T register(T in) {
        BLOCK_LIST.add(in);
        return in;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> ev) {
        IForgeRegistry<Block> reg = ev.getRegistry();
        for(Block b: BLOCK_LIST) {
            reg.register(b);
            ResourceLocation registryLocation = b.getRegistryName();
            assert registryLocation != null; // just assigned, stop complaining
            ItemBlock ib = new ItemBlock(b); // DONT FORGET ABOUT THE FUCKING ITEM ISTG
            ib.setRegistryName(registryLocation);
            GameData.register_impl(ib);
            if(b instanceof IRenderOverride) {
                ((IRenderOverride) b).registerRenderers();
            } else if (b.hasTileEntity(b.getDefaultState())) {
                if(b instanceof ViciousBlock) {
                    ViciousBlock vb = (ViciousBlock) b;
                    GameRegistry.registerTileEntity(vb.TILE, registryLocation);
                }
            }
        }
    }
}
