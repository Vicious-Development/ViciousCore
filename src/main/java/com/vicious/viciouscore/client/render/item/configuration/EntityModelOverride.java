package com.vicious.viciouscore.client.render.item.configuration;

import com.vicious.viciouscore.common.util.configuration.Config;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles entity part config management.
 * @param <T>
 */
public class EntityModelOverride<T extends ModelBase> {
    public final Map<String, ModelRendererConfiguration> modelPartConfigs = new HashMap<>();
    public EntityModelOverride(Path f, Class<T> modelclass) {
        List<Field> rendererFields = Reflection.getFieldsOfType(modelclass, ModelRenderer.class);
        for (Field rendererField : rendererFields) {
            String name = rendererField.getName();
            modelPartConfigs.put(name, new ModelRendererConfiguration(Paths.get(f.toAbsolutePath() + "/" + name + ".json")));
        }
    }
    public ModelRendererConfiguration getPartConfiguration(String fieldName){
        return modelPartConfigs.get(fieldName);
    }
}
