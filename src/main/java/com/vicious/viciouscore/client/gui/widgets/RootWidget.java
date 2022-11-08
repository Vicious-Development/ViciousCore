package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.Vector2i;

/**
 * The brain of VC Guis.
 */
public class RootWidget extends VCWidget<RootWidget>{
    public int mouseX;
    public int mouseY;
    public int mouseDX = 0;
    public int mouseDY = 0;
    public VCWidget<?> draggedWidget = null;
    public VCWidget<?> mouseWidget = addChild(new VCWidget<>(this,0,0,0,0).onlyVisible());

    public RootWidget() {
        super(null,0, 0, 0, 0);
        root=this;
        noFlags();
        addFlags(ControlFlag.RESPONDTORAYTRACE,ControlFlag.ALWAYSRENDERCHILDREN);
    }
    private void mouseUpdate(int mX, int mY){
        mouseDX = mX-mouseX;
        mouseDY = mY-mouseY;
        mouseX=mX;
        mouseY=mY;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        mouseWidget.setStartPosition(new Vector2i(mouseX,mouseY));
        mouseUpdate(mouseX,mouseY);
        widgetMouseOver();
        if(draggedWidget != null) draggedWidget.drag();
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    public void attachToMouse(VCWidget<?> widget){
        mouseWidget.addChild(widget);
    }
    public void removeFromMouse(VCWidget<?> widget){
        mouseWidget.removeChild(widget);
    }
}
