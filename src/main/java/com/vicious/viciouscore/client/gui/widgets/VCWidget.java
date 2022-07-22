package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.client.gui.GenericGUI;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.gui.components.Widget;

import java.awt.*;
import java.util.ArrayList;

public class VCWidget extends Widget implements IUpdatableWidget {
    protected ArrayList<VCWidget> children = new ArrayList<>();
    protected int mouseStartX;
    protected int mouseStartY;
    protected Vector2i startPos;
    protected Vector2i translationVector = new Vector2i(0,0);
    protected Widget draggedWidget;
    protected boolean hasBeenClicked = false;
    protected GenericGUI parent;
    public VCWidget(int x, int y, int w, int h, TextComponent text){
        super(x,y,w,h, text);
        startPos = new Vector2i(x,y);
    }
    public VCWidget(int x, int y, int w, int h, Vector2i tv, TextComponent text){
        super(x,y,w,h, text);
        startPos = tv;
        //updatePosition();
    }
    public void translate(int x, int y){
        translationVector = new Vector2i(x, y);
        for(VCWidget child : children){
            child.translate(x, y);
        }
        updatePosition();
    }
    //Called only when the screen resizes.
    //This is the same as translate, except no ZEWidget
    //Has overrided it. Shut up, these comments aren't
    //Disgusting :P. If you're reading this, have a good
    //Day
    public void handleResize(int xvec, int yvec){
        translationVector = new Vector2i(xvec,yvec);
        for(VCWidget child : children){
            child.handleResize(xvec, yvec);
        }
        x += translationVector.x;
        y += translationVector.y;
    }
    public <T extends VCWidget> T addChild(T child){
        children.add(child);
        return child;
    }
    public void addParentGUI(GenericGUI gui){
        parent = gui;
        for(VCWidget child : children){
            child.addParentGUI(gui);
        }
    }
    public ArrayList<VCWidget> getChildren(){
        return children;
    }
    protected void updatePosition(){
        x += translationVector.x;
        y += translationVector.y;
    }
    //Allows for changing the widget on resize.
    public VCWidget getUpdatedWidget(){
        VCWidget updated = new VCWidget(x, y, width, height, startPos, getMessage());
        updated.children = updateChildren();
        return updated;
    }
    public ArrayList<VCWidget> updateChildren(){
        ArrayList<VCWidget> newChildren = new ArrayList<>();
        for(VCWidget child : children){
            newChildren.add(child.getUpdatedWidget());
        }
        return newChildren;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(VCWidget child : children){
            if(child.mouseClicked(mouseX,mouseY,button)) return false;
        }
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag) {
                    this.onClick(mouseX, mouseY);
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
