package com.vicious.viciouscore.client.configuration;

import com.vicious.viciouscore.common.configuration.IOverrideConfiguration;
import com.vicious.viciouscore.common.util.file.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.model.*;
import net.minecraft.item.Item;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Powerful class designed to allow us to save and load models and render override settings while in game. This means we can edit the rendering of entities and such
 * while in game without needing to restart. It also grants other modders control over our renders.
 */
@SuppressWarnings("unchecked")
public class HeldItemOverrideCFG implements IOverrideConfiguration {
    private final Path PATH;
    private final ItemTransformOverrideCFG ITEM;
    private final Map<Class<? extends ModelBase>, EntityModelOverrideCFG<?>> MOBMAP = new HashMap<>();
    public HeldItemOverrideCFG(Item in) {
        String itemName = in.getRegistryName().toString().replaceAll(":","-");
        PATH = Directories.directorize(Directories.itemRenderOverridesDirectory.toAbsolutePath().toString(), itemName);
        FileUtil.createDirectoryIfDNE(PATH);
        ITEM = new ItemTransformOverrideCFG(Directories.directorize(PATH.toAbsolutePath().toString(), itemName + ".json"));
    }

    public static void saveAll() {
        ClientOverrideConfigurations.heldItemRenderConfigurationsMap.forEach((name, cfg)->{
            cfg.ITEM.save();
            cfg.MOBMAP.forEach((mob,override)->{
                override.saveAll();
            });
        });
    }

    public static void readAll() {
        ClientOverrideConfigurations.heldItemRenderConfigurationsMap.forEach((name, cfg) -> {
            cfg.ITEM.readFromJSON();
            cfg.MOBMAP.forEach((mob, override) -> {
                override.readAll();
            });
            cfg.ITEM.save();
        });
    }

    public static void read(String itemname) {
        HeldItemOverrideCFG cfg = ClientOverrideConfigurations.heldItemRenderConfigurationsMap.get(itemname);
        if(cfg == null) return;
        cfg.ITEM.readFromJSON();
        cfg.MOBMAP.forEach((mob, override) -> {
            override.readAll();
        });
    }

    public <T extends ModelBase> HeldItemOverrideCFG addEntityModelOverrider(Class<T> in){
        if(!ClientOverrideConfigurations.unreadableArrayLengths.containsKey(in)) {
            EntityModelOverrideCFG<T> modelconfigurator = new EntityModelOverrideCFG<T>(Directories.directorize(PATH.toAbsolutePath().toString(), in.getCanonicalName().replaceAll("\\.", "-")), in);
            MOBMAP.putIfAbsent(in, modelconfigurator);
        }
        else{
            EntityModelOverrideCFG<T> modelconfigurator = new EntityModelOverrideCFG<T>(Directories.directorize(PATH.toAbsolutePath().toString(), in.getCanonicalName().replaceAll("\\.", "-")), in, ClientOverrideConfigurations.unreadableArrayLengths.get(in));
            MOBMAP.putIfAbsent(in, modelconfigurator);
        }
        return this;
    }
    public <T extends ModelBase> EntityModelOverrideCFG<T> getEntityModelConfig(T in){
        return (EntityModelOverrideCFG<T>) MOBMAP.get(in.getClass());
    }
    public <T extends ModelBase> EntityModelOverrideCFG<T> getEntityModelConfig(Class<T> in){
        return (EntityModelOverrideCFG<T>) MOBMAP.get(in);
    }
    public ItemTransformOverrideCFG getItemConfig(){
        return ITEM;
    }

    public static void copyFromResources(String modid, Class<?> mainClass){
        FileUtil.copyResources(mainClass, "/assets/" + modid + "/itemrenderoverrides", Directories.itemRenderOverridesDirectory.toAbsolutePath().toString());
    }
}
