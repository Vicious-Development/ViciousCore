package com.vicious.viciouscore.common.util;

import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class ResourceCache implements IIconRegister, IResourceManagerReloadListener {

    public static ResourceLocation ORBSPRITELOCATION = ViciousLoader.getViciousModelTexture("galena.png");

    @Override
    public void registerIcons(TextureMap textureMap) {
        //Use in future projects for item icons.
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}
