package com.vicious.viciouscore.common.util;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.OBJParser;
import com.vicious.viciouscore.ViciousCore;
import net.minecraft.util.ResourceLocation;

public class ViciousLoader {
    public static CCModel loadModel(ResourceLocation res){
        return CCModel.combine(OBJParser.parseModels(res).values());
    }
    public static CCModel loadViciousModel(String resourcePath){
        return loadModel(getViciousResource("models/" + resourcePath));
    }
    public static ResourceLocation getViciousResource(String resourcePath){
        return new ResourceLocation(ViciousCore.MODID + ":" + resourcePath);
    }
    public static ResourceLocation getResource(String resourcePath){
        return new ResourceLocation(resourcePath);
    }
    public static ResourceLocation getItemRenderOverrideLocation(String modid){
        return getResource(modid + "/" + "itemrenderoverrides");
    }

    public static ResourceLocation getViciousTexture(String resourcePath) {
        return new ResourceLocation(ViciousCore.MODID + ":textures/" + resourcePath);
    }
    public static ResourceLocation getViciousModelTexture(String resourcePath) {
        return getViciousTexture("models/" + resourcePath);
    }
}
