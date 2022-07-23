package com.vicious.viciouscore.client.textures;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;

public class VCTextures {
    //public static final TextureAtlasSprite.Info PROGRESSARROW = createTextureInfo("textures/gui/progressarrowright.png", 32, 16);
    //public static final TextureAtlasSprite.Info ORANGE_PROGRESS_ARROW = createTextureInfo("textures/gui/orangeprogressarrowright.png", 32, 16);
    //public static final TextureAtlasSprite.Info GRINDPROGRESS = createTextureInfo("textures/gui/progress/grind.png", 32, 16);
    //public static final TextureAtlasSprite.Info GRINDPROGRESSGRAY = createTextureInfo("textures/gui/progress/grindgray.png", 32, 16);
    //public static final TextureAtlasSprite.Info MOLD_OUT_ON = createTextureInfo("textures/gui/buttons/moldouton.png", 18, 18);
    //public static final TextureAtlasSprite.Info MOLD_OUT_OFF = createTextureInfo("textures/gui/buttons/moldoutoff.png", 18, 18);
    //public static final TextureAtlasSprite.Info SLOTGB = createTextureInfo("textures/gui/slots/grayblue.png", 18, 18);
    public static final TextureAtlasSprite.Info SLOTHG = createTextureInfo("textures/gui/slots/hologreen.png", 18, 18);
    public static final TextureAtlasSprite.Info HOLOINVMKI = createTextureInfo("textures/gui/screens/holoinvmki.png", 166, 84);
    //public static final TextureAtlasSprite.Info HEATBARYELLOW = createTextureInfo("textures/gui/bars/yellowbar.png", 4, 52);
    //public static final TextureAtlasSprite.Info BLACKBARBORDER = createTextureInfo("textures/gui/blackbarborder.png", 6, 54);
    //public static final TextureAtlasSprite.Info HEATON = createTextureInfo("textures/gui/buttons/heaton.png", 18, 18);
    //public static final TextureAtlasSprite.Info FIRE = createTextureInfo("textures/gui/progress/flames.png", 12, 13);
    //public static final TextureAtlasSprite.Info FIREON = createTextureInfo("textures/gui/progress/flameson.png", 12, 13);
    //public static final TextureAtlasSprite.Info HEATOFF = createTextureInfo("textures/gui/buttons/heatoff.png", 18, 18);

    private static TextureAtlasSprite.Info createTextureInfo(String path, int w, int h){
        return new TextureAtlasSprite.Info(new ResourceLocation(ViciousCore.MODID, path),
                w,
                h,
                AnimationMetadataSection.EMPTY);
    }
}

