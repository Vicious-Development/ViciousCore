package com.vicious.viciouscore;

import com.vicious.viciouscore.common.util.resources.GameResourceHelper;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

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
        try{
            Class.forName("org.spongepowered.asm.launch.MixinBootstrap");
        } catch(ClassNotFoundException e){
            return;
        }
        MixinBootstrap.init();
        isSpongeLoaded = attemptLoadMixin("sponge");
        MixinBootstrap.init();
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

    private boolean attemptLoadMixin(String modid) {
        if(GameResourceHelper.load(modid)){
            Mixins.addConfiguration("mixin/mixins.viciouscore." + modid + ".json");
            System.out.println("ViciousCore loaded " + modid + " mixins successfully.");
            return true;
        }
        return false;
    }
}
