package com.vicious.viciouscore.common.tile;


import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//TODO: make multiblockcomponents notify each other less.
public class TileMultiBlockComponent extends ViciousTE implements INotifier<Object> {
    public List<INotifiable<Object>> parents = new ArrayList<>();
    public long lastTick = -1;
    private boolean hasBeenValidatedAlready = false;
    public TileMultiBlockComponent(){}
    @SuppressWarnings({"rawtypes","unchecked"})
    public void notifyNeighbors(){
        long worldTick = world != null ? world.getTotalWorldTime() : -1;
        forNeighborBlockTiles((bs,te,bpos)->{
            Block b = bs.getBlock();
            TileMultiBlockComponent tmbc = null;
            if(te instanceof TileMultiBlockComponent){
                tmbc = (TileMultiBlockComponent) te;
            }
            if (tmbc != null) {
                if(tmbc.lastTick != worldTick) {
                    tmbc.lastTick = worldTick;
                    List<INotifiable<Object>> parentList = tmbc.getParents();
                    for (INotifiable<Object> parent : parentList) {
                        if (!parents.contains(parent)) parents.add(parent);
                    }
                    tmbc.setParents(parents);
                }
            }
        });
    }


    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        notifyParent();
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        notifyParent();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        notifyNeighbors();
        notifyParent();
    }

    @Override
    public void validate() {
        super.validate();
        //Prevents infinite looping on chunk gen.
        if(hasBeenValidatedAlready) return;
        hasBeenValidatedAlready = true;
        notifyNeighbors();
        notifyParent();

    }

    /**
     * Notify the parent that it should do something.
     * Remove the parent if the parent is no longer a valid parent.
     */
    public void notifyParent(){
        for (int i = 0; i < parents.size(); i++) {
            INotifiable<Object> parent = parents.get(i);
            if(parent instanceof TileEntity) {
                if (((TileEntity) parent).isInvalid()) {
                    parents.remove(i);
                    i--;
                }
                else if (parent != null) {
                    parent.notify(this);
                }
            }
            else {
                parents.remove(i);
                i--;
            }
        }
    }

    @Override
    public void addParent(INotifiable<Object> parent) {
        if(!parents.contains(parent)) parents.add(parent);
    }

    @Override
    public List<INotifiable<Object>> getParents() {
        return parents;
    }

    @Override
    public void setParents(List<INotifiable<Object>> parents) {
        this.parents = parents;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        notifyParent();
        return super.getUpdatePacket();
    }
}
