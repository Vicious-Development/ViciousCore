package com.vicious.viciouscore.common.resource;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.resources.ResourceLocation;

public class VCResources {
    public static ResourceLocation COMMONKEYCAPABILITY = of("commonkeycap");
    public static ResourceLocation PLAYERDATA = of("pdcap");
    public static ResourceLocation LEVELDATA = of("ldcap");
    public static ResourceLocation GLOBALDATA = of("gdcap");
    public static ResourceLocation NETWORK = of("internalnetwork");
    public static ResourceLocation of(String key){
        return new ResourceLocation(ViciousCore.MODID,key);
    }
}
