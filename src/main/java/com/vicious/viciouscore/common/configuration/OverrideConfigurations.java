package com.vicious.viciouscore.common.configuration;

import com.vicious.viciouscore.common.util.file.Directories;
import com.vicious.viciouscore.common.worldgen.StructureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class OverrideConfigurations {
    private static Map<String, StructureConfiguration> structureConfigurations = new HashMap<>();
    public static void createStructureConfiguration(String structureName){
        structureConfigurations.putIfAbsent(structureName, new StructureConfiguration(Directories.directorize(Directories.viciousStructuresDirectory.toAbsolutePath().toString(),structureName + ".json")));
    }
    public static StructureConfiguration getStructureConfiguration(String structureName){
        return structureConfigurations.get(structureName);
    }
}
