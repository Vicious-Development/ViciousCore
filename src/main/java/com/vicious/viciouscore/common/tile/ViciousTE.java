package com.vicious.viciouscore.common.tile;

import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class ViciousTE extends TileEntity {
    public List<TileEntity> getNeighborTiles(){
        List<TileEntity> list = new ArrayList<>();
        list.add(world.getTileEntity(pos.add(0,0,1)));
        list.add(world.getTileEntity(pos.add(0,0,-1)));
        list.add(world.getTileEntity(pos.add(0,1,0)));
        list.add(world.getTileEntity(pos.add(0,-1,0)));
        list.add(world.getTileEntity(pos.add(1,0,0)));
        list.add(world.getTileEntity(pos.add(-1,0,0)));
        return list;
    }
}
