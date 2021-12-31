package com.vicious.viciouscore.common.util.file;

import net.minecraftforge.fml.common.Loader;

import java.nio.file.Path;

public class Directories {
    public static Path configDirectory;
    public static Path resourcesDirectory;
    public static Path viciousResourcesDirectory;
    public static Path viciousConfigDirectory;
    public static Path viciousCoreConfigPath;
    public static Path viciousCoreOverrideConfigPath;
    public static Path viciousStructuresDirectory;

    public static void initializeConfigDependents() {
        Directories.configDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"config"));
        Directories.resourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"resources"));
        Directories.viciousResourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(resourcesDirectory.toAbsolutePath().toString(),"vicious"));
        Directories.viciousStructuresDirectory = FileUtil.createDirectoryIfDNE(directorize(viciousResourcesDirectory.toAbsolutePath().toString(),"structures"));
        Directories.viciousConfigDirectory = FileUtil.createDirectoryIfDNE(directorize(configDirectory.toAbsolutePath().toString(),"vicious"));
        Directories.viciousCoreConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"core.json");
        Directories.viciousCoreOverrideConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"overrides.json");
    }

    public static Path directorize(String dir, String path) {
        return com.vicious.viciouslib.util.FileUtil.toPath(dir + "/" + path);
    }

    public static String rootDir() {
        String dir = Loader.instance().getConfigDir().getParent();
        return dir;
    }
}
