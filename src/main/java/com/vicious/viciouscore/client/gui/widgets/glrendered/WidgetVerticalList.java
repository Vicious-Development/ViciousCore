package com.vicious.viciouscore.client.gui.widgets.glrendered;

import com.vicious.viciouscore.client.gui.widgets.CompoundWidget;
import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.client.util.Extents;
import com.vicious.viciouscore.client.util.Vector2i;

public class WidgetVerticalList<T extends WidgetVerticalList<T>> extends CompoundWidget<T> {
    public WidgetVerticalList(RootWidget root, int x, int y, int w, int h) {
        super(root, x, y, w, h);
    }

    @Override
    public void regenerate(VCWidget<?> widget){
        int pos = 0;
        for (VCWidget<?> child : children) {
            if(child.isVisible()) {
                child.setStartPosition(new Vector2i(child.getStartPos().x, pos));
                pos += child.getHeight();
            }
        }
        Extents newEx = getDescendantExtents();
        this.setWidth(newEx.getWidth());
        this.setHeight(newEx.getHeight());
    }

    @Override
    public Extents getCompleteExtents() {
        Extents newExtents = this.extents;
        for (VCWidget<?> child : children) {
            if(child.isVisible()) newExtents = Extents.combined(newExtents, child.getCompleteExtents());
        }
        return newExtents;
    }
}
