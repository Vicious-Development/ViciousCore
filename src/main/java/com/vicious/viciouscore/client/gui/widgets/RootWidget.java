package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * The brain of VC Guis.
 */
public class RootWidget extends VCWidget<RootWidget>{
    public int mouseX;
    public int mouseY;
    public int mouseDX = 0;
    public int mouseDY = 0;
    public VCWidget<?> draggedWidget = null;

    public RootWidget() {
        super(null,0, 0, 0, 0);
        root=this;
        addFlags(ControlFlag.values());
    }
    private void mouseUpdate(int mX, int mY){
        mouseDX = mX-mouseX;
        mouseDY = mY-mouseY;
        mouseX=mX;
        mouseY=mY;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        mouseUpdate(mouseX,mouseY);
        widgetMouseOver();
        if(draggedWidget != null) draggedWidget.drag();
        super.render(stack, mouseX, mouseY, partialTicks);
    }
}
