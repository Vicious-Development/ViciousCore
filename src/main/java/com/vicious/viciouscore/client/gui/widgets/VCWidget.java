package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.Extents;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.function.Consumer;

public class VCWidget implements Widget {
    protected ArrayList<VCWidget> children = new ArrayList<>();
    protected Vector2i startPos;
    protected Vector2i offsetVector = new Vector2i(0,0);
    protected Vector2i scale = new Vector2i(1,1);
    protected Vector2i actualPosition;
    protected Vector2i actualWH;
    protected Extents extents;
    protected int height;
    protected int width;
    protected VCWidget parent;
    protected RootWidget root;
    protected boolean hasBeenClicked;
    protected boolean visible = true;
    protected boolean hovered = false;

    public VCWidget(RootWidget root, int x, int y, int w, int h){
        startPos = new Vector2i(x,y);
        this.height=h;
        this.width=w;
        this.root=root;
        calculateVectors();
    }
    public <T extends VCWidget> T copyVectors(T other){
        other.startPos=startPos;
        other.offsetVector=offsetVector;
        other.scale=scale;
        other.height=height;
        other.width=width;
        return other;
    }
    public void calcActualPosition(){
        actualPosition = startPos.add(offsetVector.multiply(scale));
        onParent((p)-> actualPosition = actualPosition.add(parent.actualPosition));
    }
    public void calcActualWidthHeight(){
        actualWH = new Vector2i(width,height).multiply(scale);
    }
    public void calcExtents(){
        this.extents = new Extents(actualPosition,actualPosition.add(actualWH));
    }
    public Extents getExtents(){
        return extents;
    }
    public <T extends VCWidget> T addChild(T child){
        children.add(child);
        child.setParent(this);
        child.calculateVectors();
        return child;
    }

    protected void setParent(VCWidget parent) {
        this.parent=parent;
    }

    /**
     * @return the widget the mouse is hovering over.
     */
    public VCWidget widgetMouseOver(){
        for (VCWidget child : children) {
            if(child.getExtents().isWithin(getMouseX(),getMouseY())){
                hovered = false;
                return child.widgetMouseOver();
            }
        }
        onHover();
        return this;
    }

    public void onHover(){
        hovered = true;
    }

    public boolean canBeHovered(){
        return false;
    }

    public void onClick(int button){
        if(canBeDragged() && leftClick(button)){
            isDraggedWidget();
        }
        hasBeenClicked=true;
    }
    public boolean leftClick(int button){
        return button == 0;
    }
    public void onRelease(int button){
        if(leftClick(button)) {
            hasBeenClicked = false;
            root.draggedWidget = null;
        }
    }
    public void playClickSound(){
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
    public void onParent(Consumer<VCWidget> cons){
        if(parent != null){
            cons.accept(parent);
        }
    }

    /**
     * @return whether dragging this widget is allowed.
     */
    public boolean canBeDragged(){
        return false;
    }
    public int getMouseX(){
        return root.mouseX;
    }
    public int getMouseY(){
        return root.mouseY;
    }
    public boolean isDraggedWidget(){
        if(root.draggedWidget == null) root.draggedWidget = this;
        return root.draggedWidget == this;
    }
    public void stopDragging() {
        root.draggedWidget = null;
    }
    public void forEachChild(Consumer<VCWidget> cons){
        children.forEach(cons);
    }
    public void translate(int x, int y){
        this.offsetVector = offsetVector.add(x,y);
        calculateVectors();
    }
    public void setScale(int x, int y){
        this.scale = new Vector2i(x,y);
        calculateVectors();
        forEachChild((c)-> c.setScale(x,y));
    }
    public void calculateVectors(){
        calcActualPosition();
        calcActualWidthHeight();
        calcExtents();
        forEachChild(VCWidget::calculateVectors);
    }
    public int getMouseDX(){
        return root.mouseDX;
    }
    public int getMouseDY(){
        return root.mouseDY;
    }
    public void drag(){
        if(!canBeDragged()) onParent(VCWidget::drag);
        else{
            translate(getMouseDX(),getMouseDY());
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        forEachChild((c)->{
            c.render(stack,mouseX,mouseY,partialTicks);
        });
    }
    public void setVisible(boolean visible){
        this.visible = visible;
        forEachChild((c)->c.setVisible(true));
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public void resize(int resizeX, int resizeY) {
        translate(resizeX,resizeY);
    }

    public long getWindowID(){
        return Minecraft.getInstance().getWindow().getWindow();
    }
}
