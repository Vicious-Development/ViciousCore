package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Powerful class designed to allow us to save and load model and render override settings while in game. This means we can edit the rendering of entities and such
 * while in game without needing to restart. It also grants other modders control over our renders.
 */
@SuppressWarnings("unchecked")
public class OverrideConfigurations {
    private final static Map<String, OverrideConfigurations> overrideConfigurationsMap = new HashMap<>();
    private final Path PATH;
    private final RenderConfiguration ITEM;
    private final Map<ModelBase, EntityModelOverride<?>> MOBMAP = new HashMap<>();
    public OverrideConfigurations(Item in) {
        String itemName = in.getRegistryName().toString();
        PATH = Paths.get(Directories.itemRenderOverridesDirectory.toAbsolutePath() + "/" + itemName);
        FileUtil.createDirectoryIfDNE(PATH);
        ITEM = new RenderConfiguration(Paths.get(PATH.toAbsolutePath() + "/" + itemName + ".json"));
    }
    public OverrideConfigurations addEntityModelOverrider(ModelBase in){
        EntityModelOverride<?> modelconfigurator = new EntityModelOverride<>(Paths.get(PATH.toAbsolutePath() + "/" + in.getClass().getCanonicalName()), in.getClass());
        MOBMAP.put(in,modelconfigurator);
        return this;
    }
    public <T extends ModelBase> EntityModelOverride<T> getEntityModelConfig(T in){
        return (EntityModelOverride<T>) MOBMAP.get(in);
    }
    public RenderConfiguration getItemConfig(){
        return ITEM;
    }
    public static OverrideConfigurations create(Item in){
        OverrideConfigurations cfg = new OverrideConfigurations(in);
        overrideConfigurationsMap.put(in.getRegistryName().toString(), cfg);
        return cfg;
    }
    public static OverrideConfigurations getConfiguration(Item in){
        return overrideConfigurationsMap.get(in.getRegistryName().toString());
    }
}
