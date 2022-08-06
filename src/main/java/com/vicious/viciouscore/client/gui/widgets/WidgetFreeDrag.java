package com.vicious.viciouscore.client.gui.widgets;


import net.minecraft.resources.ResourceLocation;


public class WidgetFreeDrag extends WidgetImage{

    public WidgetFreeDrag(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource) {
        super(root, x, y, w, h, widgetResource);
    }

    @Override
    public boolean canBeDragged() {
        return true;
    }

    @Override
    public boolean canBeHovered(){
        return false;
    }
}
