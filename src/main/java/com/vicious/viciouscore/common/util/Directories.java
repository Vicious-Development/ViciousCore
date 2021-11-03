package com.vicious.viciouscore.common.util;

import com.vicious.viciouscore.common.util.file.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.Sys;

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
        Directories.configDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"config"));
        Directories.resourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(rootDir(),"resources"));
        Directories.viciousResourcesDirectory = FileUtil.createDirectoryIfDNE(directorize(resourcesDirectory.toAbsolutePath().toString(),"vicious"));
        Directories.itemRenderOverridesDirectory = FileUtil.createDirectoryIfDNE(directorize(viciousResourcesDirectory.toAbsolutePath().toString(),"itemrenderoverrides"));
        Directories.viciousConfigDirectory = FileUtil.createDirectoryIfDNE(directorize(configDirectory.toAbsolutePath().toString(),"vicious"));
        Directories.viciousCoreConfigPath = directorize(viciousConfigDirectory.toAbsolutePath().toString(),"viciouscore.json");
    }

    public static Path directorize(String dir, String path) {

        return Paths.get(dir + "\\" + path);
    }

    public static String rootDir() {
        String dir =  Loader.instance().getConfigDir().getParent();
        System.out.println("ROOT: " + dir);
        return dir;
    }
}
