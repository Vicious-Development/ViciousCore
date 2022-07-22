package com.vicious.viciouscore.common.util.file;

import java.nio.file.Path;

public class ViciousDirectories {
    public static Path configDirectory;
    public static Path resourcesDirectory;
    public static Path viciousResourcesDirectory;
    public static Path viciousConfigDirectory;
    public static Path viciousCoreConfigPath;
    public static Path viciousCoreOverrideConfigPath;
    public static Path viciousStructuresDirectory;

    public static void initializeConfigDependents() {
        ViciousDirectories.configDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"config"));
        ViciousDirectories.resourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"resources"));
        ViciousDirectories.viciousResourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(resourcesDirectory.toAbsolutePath().toString(),"vicious"));
        ViciousDirectories.viciousStructuresDirectory = FileUtil.createDirectoryIfDNE(directorize(viciousResourcesDirectory.toAbsolutePath().toString(),"structures"));
        ViciousDirectories.viciousConfigDirectory = FileUtil.createDirectoryIfDNE(directorize(configDirectory.toAbsolutePath().toString(),"vicious"));
        ViciousDirectories.viciousCoreConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"core.json");
        ViciousDirectories.viciousCoreOverrideConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"overrides.json");
    }
    public static Path createConfigPath(String modid){
        return directorize(configDirectory.toAbsolutePath().toString(), modid + ".json");
    }

    public static Path directorize(String dir, String path) {
        return com.vicious.viciouslib.util.FileUtil.toPath(dir + "/" + path);
    }

    public static String rootDir() {
        String dir = ".";
        return dir;
    }
}
