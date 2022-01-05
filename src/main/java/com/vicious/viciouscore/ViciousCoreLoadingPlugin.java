package com.vicious.viciouscore;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;


/**
 * Used only when Sponge is present. Sponge makes mixin modifications to the chunk class which interfere with the tile entity overriding system.
 */
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class ViciousCoreLoadingPlugin implements IFMLLoadingPlugin
{
    public static boolean isSpongeLoaded = false;
    public ViciousCoreLoadingPlugin()
    {
    }
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }
    @Override
    public String getModContainerClass()
    {
        return null;
    }
    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }
    @Override
    public void injectData(Map<String, Object> data)
    {

    }
    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

}
