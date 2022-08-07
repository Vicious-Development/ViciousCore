package com.vicious.viciouscore.client.textures;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;

public class VCTextures {
    public static final TextureAtlasSprite.Info SLOTHG = createTextureInfo("textures/gui/slots/hologreen.png", 18, 18);
    public static final TextureAtlasSprite.Info SLOTHGS = createTextureInfo("textures/gui/slots/hologreenselected.png", 18, 18);
    public static final TextureAtlasSprite.Info HOLOINVMKI = createTextureInfo("textures/gui/screens/holoinvmki.png", 166, 84);

    private static TextureAtlasSprite.Info createTextureInfo(String path, int w, int h){
        return new TextureAtlasSprite.Info(new ResourceLocation(ViciousCore.MODID, path),
                w,
                h,
                AnimationMetadataSection.EMPTY);
    }
}

