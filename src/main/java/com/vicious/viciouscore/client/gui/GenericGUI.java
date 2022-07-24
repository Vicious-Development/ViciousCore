package com.vicious.viciouscore.client.gui;


import com.vicious.viciouscore.client.gui.widgets.RootWidget;
import com.vicious.viciouscore.client.gui.widgets.VCWidget;
import com.vicious.viciouscore.client.gui.widgets.WidgetFreeDrag;
import com.vicious.viciouscore.client.gui.widgets.WidgetSlot;
import com.vicious.viciouscore.client.textures.VCTextures;
import com.vicious.viciouscore.client.util.WindowGetter;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import com.vicious.viciouscore.common.inventory.slots.VCSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Generic GUI used to create a default GUI system which means I can fabricate guis rather than create a gui image for every gui I'll need.
 */
public abstract class GenericGUI<T extends GenericContainer<T>> extends AbstractContainerScreen<T> {
    protected RootWidget root = new RootWidget();
    protected int resizeX;
    protected int resizeY;
    protected boolean initialized = false;
    protected int prevX = 0, prevY = 0;
    protected int guiTop = 0, guiLeft = 0;

    public GenericGUI(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        prevX = WindowGetter.window.getWidth()/2/(int)WindowGetter.window.getGuiScale();
        prevY = WindowGetter.window.getHeight()/2/(int)WindowGetter.window.getGuiScale();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        root.mouseX = (int) mx;
        root.mouseY = (int) my;
        root.widgetMouseOver().onClick(button);
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        root.mouseX = (int) mx;
        root.mouseY = (int) my;
        root.widgetMouseOver().onRelease(button);
        return super.mouseReleased(mx, my, button);
    }

    public Slot getHoveredSlot(){
        return hoveredSlot;
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
    protected void createHoloInv() {
        //Holoinvbackscreen.
        WidgetFreeDrag holo = new WidgetFreeDrag(root,menu.playerSlots.get(0).x - 7,menu.playerSlots.get(0).y - 66, VCTextures.HOLOINVMKI.width(), VCTextures.HOLOINVMKI.height(), VCTextures.HOLOINVMKI.name());
        for(VCSlot slot : menu.playerSlots){
            holo.addChild(new WidgetSlot(root,slot.x-1,  slot.y-1, VCTextures.SLOTHG.width(), VCTextures.SLOTHG.height(), VCTextures.SLOTHG.name(), slot, menu));
        }
    }
    protected <W extends VCWidget> W add(W widget){
        root.addChild(widget);
        return widget;
    }
}
