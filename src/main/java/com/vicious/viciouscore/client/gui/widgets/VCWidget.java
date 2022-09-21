package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.Extents;
import com.vicious.viciouscore.client.util.Vector2f;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VCWidget implements Widget {
    protected ArrayList<VCWidget> children = new ArrayList<>();
    protected Vector2i startPos;
    protected Vector2i offsetVector = new Vector2i(0,0);
    protected Vector2f scale = new Vector2f(1,1);
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
    protected boolean alertUpdates = true;

    public void shouldUpdate(boolean doUpdates){
        this.alertUpdates =doUpdates;
    }


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
        actualPosition = startPos.add(offsetVector);
        onParent((p)-> actualPosition = actualPosition.add(parent.actualPosition));
    }
    public void calcActualWidthHeight(){
        actualWH = new Vector2i(width,height);
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
            if(child.respondToInputs() && child.getExtents().isWithin(getMouseX(),getMouseY())){
                hovered = false;
                return child.widgetMouseOver();
            }
        }
        if(canBeHovered()) onHover();
        return this;
    }

    public boolean respondToInputs(){
        return true;
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
    public void setScale(float x, float y){
        this.scale = new Vector2f(x,y);
    }
    public void calculateVectors(){
        Extents pre = extents;
        calcActualPosition();
        calcActualWidthHeight();
        calcExtents();
        forEachChild(VCWidget::calculateVectors);
        if(alertUpdates) {
            if (!getExtents().equals(pre)) {
                for (Consumer<VCWidget> listener : listeners) {
                    listener.accept(this);
                }
            }
        }
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

    /**
     * Called before the children render.
     */
    protected void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks){

    }

    /**
     * Called before this widget and its children start rendering.
     */
    protected void doGLTransformations(PoseStack stack){
        stack.pushPose();
        if(scale.x != 1 || scale.y != 1){
            stack.scale(scale.x,scale.y,1.0F);
        }
    }

    /**
     * Called after this widget and its children finish rendering.
     */
    protected void undoGLTransformations(PoseStack stack){
        stack.popPose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        doGLTransformations(stack);
        if(isVisible()) renderWidget(stack,mouseX,mouseY,partialTicks);
        forEachChild((c)->{
            c.render(stack,mouseX,mouseY,partialTicks);
        });
        hovered=false;
        undoGLTransformations(stack);
    }

    public boolean isVisible(){
        return visible;
    }
    public void setVisible(boolean visible){
        this.visible = visible;
        forEachChild((c)->c.setVisible(visible));
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

    public void setStartPosition(Vector2i vec) {
        this.startPos=vec;
        calculateVectors();
    }

    public void setWidth(int width){
        this.width=width;
        calculateVectors();
    }
    public void setHeight(int height){
        this.height=height;
        calculateVectors();
    }

    /**
     * @return The combined extents of the descendents and this widget's extents.
     */
    public Extents getCompleteExtents(){
        return Extents.combined(getDescendantExtents(),extents);
    }
    public Extents getDescendantExtents(){
        Extents newExtents = null;
        for (VCWidget child : children) {
            newExtents = Extents.combined(newExtents,child.getCompleteExtents());
        }
        return newExtents;
    }

    protected List<Consumer<VCWidget>> listeners = new ArrayList<>();
    public void listen(Consumer<VCWidget> listener){
        listeners.add(listener);
    }
    public void stopListening(Consumer<VCWidget> listener){
        listeners.remove(listener);
    }
    public Vector2i getStartPos(){
        return startPos;
    }
}
