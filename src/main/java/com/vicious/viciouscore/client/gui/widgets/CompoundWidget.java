package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.client.util.Extents;

/**
 * These widgets are intended to automatically generate their extents based on the children they have.
 */
public class CompoundWidget<T extends CompoundWidget<T>> extends VCWidget<T>{
    public CompoundWidget(RootWidget root, int x, int y, int w, int h) {
        super(root, x, y, w, h);
    }
    @Override
    public <V extends VCWidget<?>> V addChild(V child) {
        child.listen(this::startRegeneration);
        child.addFlags(ControlFlag.SHOULDBROADCASTUPDATES);
        addFlags(ControlFlag.RESPONDTOCHILDUPDATES);
        return super.addChild(child);
    }
    private boolean isRegenerating = false;
    protected final void startRegeneration(VCWidget<?> cause){
        if(isRegenerating) return;
        regenerate(cause);
        isRegenerating = false;
    }
    public void regenerate(VCWidget<?> cause){
        Extents e = getDescendantExtents();
        this.setWidth(e.getWidth());
        this.setHeight(e.getHeight());
    }
}
