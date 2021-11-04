package com.vicious.viciouscore.client.configuration;

import com.vicious.viciouscore.common.util.Directories;
import com.vicious.viciouscore.common.util.file.FileUtil;
import com.vicious.viciouscore.common.util.reflect.MappingsReference;
import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles entity part config management.
 * @param <T>
 */
public class EntityModelOverrideCFG<T extends ModelBase> {
    private final Class<?> modelclass;
    public final Map<String, EntityPartTransformOverrideCFG> modelPartConfigs = new HashMap<>();

    public EntityModelOverrideCFG(Path f, Class<T> modelclass) {
        this.modelclass=modelclass;
        FileUtil.createDirectoryIfDNE(f);
        List<Field> rendererFields = Reflection.getFieldsOfType(modelclass, ModelRenderer.class);
        for (Field rendererField : rendererFields) {
            String name = rendererField.getName();
            String mcp = MappingsReference.toMCP(modelclass,name);
            name = mcp != null ? mcp : name;
            modelPartConfigs.putIfAbsent(name, new EntityPartTransformOverrideCFG(Directories.directorize(f.toAbsolutePath().toString(), name + ".json")));
        }
    }

    /**
     * For when the model has arrays,
     */
    public EntityModelOverrideCFG(Path f, Class<T> modelclass, int[] arraySizes) {
        this.modelclass=modelclass;
        FileUtil.createDirectoryIfDNE(f);
        List<Field> rendererFields = Reflection.getFieldsOfType(modelclass, ModelRenderer.class);
        int i = 0;
        for (Field rendererField : rendererFields) {
            String name = rendererField.getName();
            String mcp = MappingsReference.toMCP(modelclass,name);
            name = mcp != null ? mcp : name;
            if(rendererField.getType().isArray()){
                for (int j = 0; j < arraySizes[i]; j++) {
                    name = rendererField.getName() + j;
                    modelPartConfigs.putIfAbsent(name, new EntityPartTransformOverrideCFG(Directories.directorize(f.toAbsolutePath().toString(),name + ".json")));
                }
                i++;
            }
            else modelPartConfigs.putIfAbsent(name, new EntityPartTransformOverrideCFG(Directories.directorize(f.toAbsolutePath().toString(), name + ".json")));
        }
    }
    public EntityPartTransformOverrideCFG getPartConfiguration(String fieldName){
        String mcp = MappingsReference.toMCP(modelclass,fieldName);
        return modelPartConfigs.get(mcp != null ? mcp : fieldName);
    }

    public void saveAll() {
        modelPartConfigs.forEach((name,cfg)->{
            cfg.save();
        });
    }

    public void readAll() {
        modelPartConfigs.forEach((name,cfg)->{
            cfg.readFromJSON();
            cfg.save();
        });
    }
}
