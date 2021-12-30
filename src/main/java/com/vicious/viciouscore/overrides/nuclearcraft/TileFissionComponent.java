package com.vicious.viciouscore.overrides.nuclearcraft;

import com.vicious.viciouscore.common.tile.INotifiable;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;

public class TileFissionComponent extends TileMultiBlockComponent {
    @Override
    public void addParent(INotifiable<Object> parent) {
        super.addParent(parent);
        notifyNeighbors();
    }
}
