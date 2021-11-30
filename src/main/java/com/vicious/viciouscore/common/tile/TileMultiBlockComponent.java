package com.vicious.viciouscore.common.tile;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class TileMultiBlockComponent extends ViciousTE implements INotifier<Object> {
    public List<INotifiable<Object>> parents;

    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    /**
     * This may add blocks outside of the multiblock to the structure, but this is ok. It won't really harm the server too bad.
     */
    public void onLoad() {
        super.onLoad();
        for (TileEntity neighborTile : getNeighborTiles()) {
            if(neighborTile instanceof INotifier) {
                List<INotifiable> parentList = ((INotifier) neighborTile).getParents();
                for (INotifiable parent : parentList) {
                    if(!parents.contains(parent)) parents.add(parent);
                }
                ((INotifier) neighborTile).setParents(parents);
            }
        }
        notifyParent();
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
        notifyParent();
    }

    @Override
    public void validate() {
        super.validate();
        notifyParent();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        notifyParent();
    }

    /**
     * Notify the parent that it should do something.
     * Remove the parent if the parent is no longer a valid parent.
     */
    public void notifyParent(){
        for (int i = 0; i < parents.size(); i++) {
            INotifiable<Object> parent = parents.get(i);
            if(parent instanceof TileEntity){
                if(((TileEntity) parent).isInvalid()){
                    parents.remove(i);
                    i--;
                }
            }
            else if (parent != null) {
                parent.notify(this);
            }
            else {
                parents.remove(i);
                i--;
            }
        }
    }

    @Override
    public void addParent(INotifiable<Object> parent) {
        parents.add(parent);
    }

    @Override
    public List<INotifiable<Object>> getParents() {
        return parents;
    }

    @Override
    public void setParents(List<INotifiable<Object>> parents) {
        this.parents = parents;
    }
}
