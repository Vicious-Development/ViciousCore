package com.vicious.viciouscore.common.structure;

import com.vicious.viciouscore.common.configuration.StructureComponentConfiguration;
import com.vicious.viciouscore.common.player.ViciousCorePlayerInfo;
import com.vicious.viciouscore.common.player.ViciousCorePlayerManager;
import com.vicious.viciouscore.common.util.Directories;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.nio.file.Path;
import java.util.UUID;

public class ViciousCoreStructureSystem {


    public static void saveStructure(String name, UUID uuid) {
        ViciousCorePlayerInfo pinfo = ViciousCorePlayerManager.getPInfo(uuid);
        BlockPos pos1 = pinfo.pos1;
        BlockPos pos2 = pinfo.pos2;
        World world = pinfo.world;
        if(pos1 == null || pos2 == null || world == null) throw new NullPointerException("You haven't set positions!");
        pinfo.loadedStructure = new StructureComponentConfiguration(name);
        pinfo.loadedStructure.writeBlocks(world,pos1,pos2);
    }
    public static void loadStructure(String name, UUID uuid){
        ViciousCorePlayerManager.pinfo.get(uuid).loadedStructure = new StructureComponentConfiguration(name);
    }
    public static void editLoadedStructure(EntityPlayer plr){
        ViciousCorePlayerInfo pinfo = ViciousCorePlayerManager.getPInfo(plr.getUniqueID());
        StructureComponentConfiguration loadedStructure = pinfo.loadedStructure;
        if(loadedStructure == null) throw new NullPointerException("You don't have a structure loaded!");
        pinfo.confirmableTask = ()-> {
            loadedStructure.replaceBlocksInWorld(plr.world,plr.getPosition());
            pinfo.pos1 = loadedStructure.pos1();
            pinfo.pos2 = loadedStructure.pos2();
            pinfo.world = plr.world;
        };
    }
    public static void saveLoadedStructure(UUID uuid){
        StructureComponentConfiguration loadedStructure = ViciousCorePlayerManager.pinfo.get(uuid).loadedStructure;
        if(loadedStructure == null) throw new NullPointerException("You don't have a structure loaded!");
        loadedStructure.writeBlocks(ViciousCorePlayerManager.getPInfo(uuid).world, ViciousCorePlayerManager.pinfo.get(uuid).pos1, ViciousCorePlayerManager.pinfo.get(uuid).pos2);
    }


    private static Path getStructurePath(String name){
        return Directories.directorize(Directories.viciousStructuresDirectory.toAbsolutePath().toString(),name);
    }

    public static void pasteStructure(EntityPlayer plr) {
        ViciousCorePlayerInfo pinfo = ViciousCorePlayerManager.getPInfo(plr.getUniqueID());
        pinfo.loadedStructure.replaceBlocksInWorld(pinfo.world,pinfo.pos1);
    }
}
