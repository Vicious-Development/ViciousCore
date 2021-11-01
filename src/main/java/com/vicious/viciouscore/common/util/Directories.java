package com.vicious.viciouscore.common.util;

import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Directories {
    public static Path configDirectory;
    public static Path resourcesDirectory;
    public static Path viciousResourcesDirectory;
    public static Path viciousConfigDirectory;
    public static Path viciousCoreConfigPath;
    public static Path itemRenderOverridesDirectory;

    public static void initializeConfigDependents() {
        Directories.configDirectory = FileUtil.createDirectoryIfDNE(Paths.get(rootDir() + "/config"));
        Directories.resourcesDirectory = FileUtil.createDirectoryIfDNE(Paths.get(rootDir() + "/resources"));
        Directories.viciousResourcesDirectory = FileUtil.createDirectoryIfDNE(Paths.get(resourcesDirectory.toAbsolutePath() + "/vicious"));
        Directories.itemRenderOverridesDirectory = FileUtil.createDirectoryIfDNE(Paths.get(viciousResourcesDirectory.toAbsolutePath() + "/itemrenderoverrides"));
        Directories.viciousConfigDirectory = FileUtil.createDirectoryIfDNE(Paths.get(configDirectory.toAbsolutePath() + "/vicious"));
        Directories.viciousCoreConfigPath = FileUtil.createDirectoryIfDNE(Paths.get(configDirectory.toAbsolutePath() + "/viciouscore.json"));

    }

    public static String rootDir() {
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("/.","");
    }
}
