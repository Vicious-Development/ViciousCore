package com.vicious.viciouscore.client.gui;


import com.mojang.blaze3d.systems.RenderSystem;
import com.vicious.viciouscore.client.gui.widgets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.Slot;

import java.awt.*;
import java.util.ArrayList;

/**
 * Generic GUI used to create a default GUI system which means I can fabricate guis rather than create a gui image for every gui I'll need.
 */
public abstract class GenericGUI<T extends Container> extends ContainerScreen {
    protected ArrayList<VCWidget> widgets = new ArrayList<>();
    protected int resizeX;
    protected int resizeY;
    protected int curMouseX = 0, curMouseY = 0;
    protected boolean initialized = false;
    protected boolean truelyInitialized = false;
    protected int prevX = 0, prevY = 0;
    public GenericGUI(T screenContainer, PlayerInventory inv, TextComponent titleIn) {
        super(screenContainer, new ZEPlayerInventory(inv.player), titleIn);
    }
    public Slot getHoveredSlot(){
        return hoveredSlot;
    }
    public void initWidgets() {
        if (!truelyInitialized) {
            createFirst();
        } else {
            for (VCWidget widget : widgets) {
                if (widget instanceof IUpdatableWidget) {
                    int index = widgets.indexOf(widget);
                    widgets.set(index, widget.getUpdatedWidget());
                }
            }
        }
        for (VCWidget widget : widgets) {
            widget.addParentGUI(this);
            addButton(widget);
            for (ZEWidget child : widget.getChildren()) {
                addButton(child);
            }
            widget.handleResize(resizeX, resizeY);
        }
        resizeX = 0;
        resizeY = 0;
        initialized = true;
    }
    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        initialized = false;
        resizeX += (width /2 - prevX);
        resizeY += (height /2 - prevY);
        prevX = width/2;
        prevY = height/2;
        super.resize(minecraft, width, height);
    }
    public void updateWidgets(){
        if(initialized) {
            for (int i = 0; i < widgets.size(); i++) {
                ZEWidget widget = widgets.get(i);
                if (widget instanceof WidgetDraggable) {
                    ((WidgetDraggable) widget).attemptDrag(curMouseX, curMouseY);
                }
            }
        }
        else{
            initWidgets();
        }
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.defaultAlphaFunc();

        //Create the widgets that we will need. NOTE: this system is put in place to enable dragging widgets. Some widgets are not draggable and therefore don't require this.
        updateWidgets();
    }
    public abstract void createFirst();
    public static final StringTextComponent BLANK = new StringTextComponent(""); //Used to simplify. Do not change ever.
    @Override
    public TextComponent getTitle(){
        return BLANK;
    }

    protected void createHoloInv(GenericContainer container) {
        //Holoinvbackscreen.
        WidgetFreeDrag holo = add(new WidgetFreeDrag(guiLeft + container.inventorySlots.get(0).xPos - 7, guiTop + container.inventorySlots.get(0).yPos - 66, ZETextureRegistry.HOLOINVMKI.getSpriteWidth(), ZETextureRegistry.HOLOINVMKI.getSpriteHeight(), BLANK, ZETextureRegistry.HOLOINVMKI.getSpriteLocation()));
        for(ZESlot slot : (ArrayList<ZESlot>) container.playerSlots){
            holo.addChild(new WidgetSlot(guiLeft + slot.xPos-1, guiTop + slot.yPos-1, ZETextureRegistry.SLOTHG.getSpriteWidth(), ZETextureRegistry.SLOTHG.getSpriteHeight(), BLANK, ZETextureRegistry.SLOTHG.getSpriteLocation(), slot, container));
        }
    }
    protected <Z extends ZEWidget> Z add(Z widget){
        widgets.add(widget);
        return widget;
    }
}
