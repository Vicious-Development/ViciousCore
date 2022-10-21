package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.Extents;
import com.vicious.viciouscore.client.util.Vector2f;
import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.*;
import java.util.function.Consumer;

public class VCWidget<T extends VCWidget<T>> implements Widget {
    public T asT(){
        return (T) this;
    }

    protected Set<VCWidget<?>> children = new HashSet<>();

    protected Vector2i startPos;
    public void setStartPosition(Vector2i vec) {
        this.startPos=vec;
        calculateVectors();
    }
    public Vector2i getStartPos(){
        return startPos;
    }



    protected Vector2i offsetVector = new Vector2i(0,0);
    public void translate(int x, int y){
        this.offsetVector = offsetVector.add(x,y);
        calculateVectors();
    }



    protected Vector2f scale = new Vector2f(1,1);
    protected Vector2i actualPosition;

    protected Vector2i actualWH;

    /**
     * EXTENTS: Used to determine the actual area a widget occupies.
     */
    protected Extents extents;
    public void calcExtents(){
        this.extents = new Extents(actualPosition,actualPosition.add(actualWH));
    }
    public Extents getExtents(){
        return extents;
    }
    /**
     * @return The combined extents of the descendents and this widget's extents.
     */
    public Extents getCompleteExtents(){
        return Extents.combined(getDescendantExtents(),extents);
    }
    public Extents getDescendantExtents(){
        Extents newExtents = null;
        for (VCWidget<?> child : children) {
            newExtents = Extents.combined(newExtents,child.getCompleteExtents());
        }
        return newExtents;
    }
    /**
     * DIMENSIONS: Used to determine width and height of a widget
     */
    protected Vector2i dimensions = new Vector2i(0,0);
    public int getWidth(){
        return dimensions.x;
    }
    public int getHeight(){
        return dimensions.y;
    }
    public T dimensions(Vector2i dimensions){
        this.dimensions=dimensions;
        calculateVectors();
        return asT();
    }
    public T setWidth(int width){
        this.dimensions = dimensions.withX(width);
        calculateVectors();
        return asT();
    }
    public T setHeight(int height){
        this.dimensions = dimensions.withY(height);
        calculateVectors();
        return asT();
    }

    /**
     * PARENT: The widget which this widget has been added to.
     */
    protected VCWidget<?> parent;
    protected void setParent(VCWidget<?> parent) {
        this.parent=parent;
    }
    public VCWidget<?> getParent(){
        return parent;
    }
    /**
     * A set of flags determining how the widget should act.
     */
    protected Set<ControlFlag> controlFlags = EnumSet.of(ControlFlag.RESPONDTORAYTRACE);
    public T addFlags(ControlFlag... respondTos){
        for (ControlFlag respondTo : respondTos) {
            this.controlFlags.add(respondTo);
        }
        return asT();
    }
    public T removeFlags(ControlFlag... respondTos){
        for (ControlFlag respondTo : respondTos) {
            this.controlFlags.remove(respondTo);
        }
        return asT();
    }
    public boolean hasFlag(ControlFlag respondTo){
        return this.controlFlags.contains(respondTo);
    }

    protected RootWidget root;
    protected List<Consumer<VCWidget<?>>> listeners = new ArrayList<>();

    public VCWidget(RootWidget root, int x, int y, int w, int h){
        startPos = new Vector2i(x,y);
        this.dimensions =new Vector2i(w,h);
        this.root=root;
        calculateVectors();
        addGL(RenderStage.PRE,(stack)->{
            if(scale.x != 1 || scale.y != 1){
                stack.scale(scale.x,scale.y,1.0F);
            }
        });
    }
    public void calcActualPosition(){
        actualPosition = startPos.add(offsetVector);
        onParent((p)-> actualPosition = actualPosition.add(parent.actualPosition));
    }
    public void calcActualWidthHeight(){
        actualWH = new Vector2i(dimensions);
    }

    public <V extends VCWidget<?>> V addChild(V child){
        children.add(child);
        child.setParent(this);
        child.calculateVectors();
        return child;
    }


    /**
     * @return the widget the mouse is hovering over.
     */
    public VCWidget<?> widgetMouseOver(){
        for (VCWidget<?> child : children) {
            if(hasFlag(ControlFlag.RESPONDTORAYTRACE) && child.getExtents().isWithin(getMouseX(),getMouseY())){
                removeFlags(ControlFlag.HOVERED);
                return child.widgetMouseOver();
            }
        }
        if(hasFlag(ControlFlag.RESPONDTOHOVER)){
            addFlags(ControlFlag.HOVERED);
        }
        return this;
    }

    public void onClick(int button){
        if(hasFlag(ControlFlag.RESPONDTODRAG) && leftClick(button)){
            isDraggedWidget();
        }
        addFlags(ControlFlag.CLICKED);
    }
    public boolean leftClick(int button){
        return button == 0;
    }
    public void onRelease(int button){
        if(leftClick(button)) {
            removeFlags(ControlFlag.CLICKED);
            root.draggedWidget = null;
        }
    }
    public void playClickSound(){
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
    public void onParent(Consumer<VCWidget<?>> cons){
        if(parent != null){
            cons.accept(parent);
        }
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
    public void forEachChild(Consumer<VCWidget<?>> cons){
        children.forEach(cons);
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
        if(hasFlag(ControlFlag.SHOULDBROADCASTUPDATES)) {
            if (!getExtents().equals(pre)) {
                for (Consumer<VCWidget<?>> listener : listeners) {
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
        if(!hasFlag(ControlFlag.RESPONDTODRAG)) onParent(VCWidget::drag);
        else{
            translate(getMouseDX(),getMouseDY());
        }
    }

    /**
     * Called before the children render.
     */
    protected void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks){

    }

    protected Map<RenderStage,List<Consumer<PoseStack>>> glTransformers = new HashMap<>();
    public T addGL(RenderStage stage, Consumer<PoseStack> cons){
        List<Consumer<PoseStack>> lst = glTransformers.computeIfAbsent(stage,k->new ArrayList<>());
        lst.add(cons);
        return asT();
    }
    public void applyGL(RenderStage stage, PoseStack stack){
        for (Consumer<PoseStack> c : glTransformers.get(stage)) {
            c.accept(stack);
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        stack.pushPose();
        applyGL(RenderStage.PRE,stack);
        if(hasFlag(ControlFlag.VISIBLE)){
            stack.pushPose();
            applyGL(RenderStage.SELFPRE,stack);
            renderWidget(stack,mouseX,mouseY,partialTicks);
            applyGL(RenderStage.SELFPOST,stack);
            stack.popPose();
        }
        forEachChild((c)->{
            c.render(stack,mouseX,mouseY,partialTicks);
        });
        applyGL(RenderStage.POST,stack);
        stack.popPose();
        removeFlags(ControlFlag.HOVERED);
    }

    public void resize(int resizeX, int resizeY) {
        translate(resizeX,resizeY);
    }
    public long getWindowID(){
        return Minecraft.getInstance().getWindow().getWindow();
    }


    public void listen(Consumer<VCWidget<?>> listener){
        listeners.add(listener);
    }
    public void stopListening(Consumer<VCWidget<?>> listener){
        listeners.remove(listener);
    }

    public void removeChild(VCWidget<?> widget){
        children.remove(widget);
    }
    public boolean hasChild(VCWidget<?> widget){
        return children.contains(widget);
    }

    public boolean isCoveredAt(int x, int y){
        VCWidget<?> parent = getParent();
        VCWidget<?> previous = this;
        while(parent != null) {
            for (VCWidget<?> child : parent.children) {
                if (child != previous) {
                    if (child.getExtents().isWithin(x, y)) {
                        return true;
                    } else {
                        previous=parent;
                        parent = parent.getParent();
                    }
                }
            }
        }
        return false;
    }

    public boolean isExposedAt(int x, int y){
        return !isCoveredAt(x,y);
    }

    public T noFlags() {
        controlFlags.clear();
        return asT();
    }
}

