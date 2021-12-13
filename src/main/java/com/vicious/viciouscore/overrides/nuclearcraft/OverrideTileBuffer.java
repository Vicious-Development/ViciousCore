package com.vicious.viciouscore.overrides.nuclearcraft;

import com.google.common.collect.Lists;
import com.vicious.viciouscore.common.tile.INotifiable;
import com.vicious.viciouscore.common.tile.INotifier;
import com.vicious.viciouscore.common.util.reflect.IFieldCloner;
import nc.tile.energyFluid.TileBuffer;

import java.util.List;

public class OverrideTileBuffer extends TileBuffer implements INotifier<Object>, IFieldCloner {
    private INotifiable<Object> parent;
    public OverrideTileBuffer(Object og){
        clone(og);
    }
    public OverrideTileBuffer() {
        super();
    }

    @Override
    public void notifyParent() {
        if(parent != null) parent.notify(this);
    }

    @Override
    public void addParent(INotifiable<Object> parent) {
        this.parent=parent;
    }

    @Override
    public List<INotifiable<Object>> getParents() {
        return Lists.newArrayList(parent);
    }

    @Override
    public void setParents(List<INotifiable<Object>> parents) {
        if(parents == null || parents.isEmpty()) return;
        this.parent=parents.get(0);
    }
}