package com.vicious.viciouscore.common.registries;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.client.render.IRenderOverride;
import com.vicious.viciouscore.common.sampleblock.SampleBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ViciousCore.MODID)
public class VBlockRegistry extends Registrator {
    private static final List<Block> BLOCK_LIST = new ArrayList<>();
    public static final SampleBlock SAMPLE_BLOCK = register(new SampleBlock("sampleblock"));
    public static <T extends Block> T register(T in){
        BLOCK_LIST.add(in);
        return in;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> ev) {
        IForgeRegistry<Block> reg = ev.getRegistry();
        for(Block b: BLOCK_LIST) {
            reg.register(b);
            if(b instanceof IRenderOverride) {
                ((IRenderOverride)b).registerRenderers();
            }
        }
    }
}
