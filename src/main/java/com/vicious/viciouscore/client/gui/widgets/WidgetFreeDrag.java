package com.vicious.viciouscore.client.gui.widgets;


import net.minecraft.resources.ResourceLocation;


public class WidgetFreeDrag<T extends WidgetFreeDrag<T>> extends WidgetImage<T>{
    public WidgetFreeDrag(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource) {
        super(root, x, y, w, h, widgetResource);
        hasFlag(ControlFlag.RESPONDTODRAG);
    }
}
