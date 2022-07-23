package com.vicious.viciouscore.client.gui.widgets;


import net.minecraft.resources.ResourceLocation;

public class WidgetButton extends WidgetImage {
    private boolean doClick = true;

    public WidgetButton(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource) {
        super(root, x, y, w, h, widgetResource);
    }

    @Override
    public void onClick(int button) {
        if(!doClick){
            doClick = true;
            return;
        }
        playClickSound();
//        ZENetwork.simplechannel.sendToServer(new CClickContainerButton(TILEPOS,ID));
        doClick = false;
    }
}
