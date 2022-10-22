package com.vicious.viciouscore.client.gui.widgets;


import net.minecraft.resources.ResourceLocation;

public class WidgetButton<T extends WidgetButton<T>> extends WidgetImage<T> {
    private boolean doClick = true;

    public WidgetButton(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource) {
        super(root, x, y, w, h, widgetResource);
        addFlags(ControlFlag.RESPONDTOCLICK);
    }

    @Override
    public void onClick(int button) {
        if(!doClick){
            doClick = true;
            return;
        }
        playClickSound();
        doClick = false;
    }
}
