package com.vicious.viciouscore.client.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.gui.widgets.*;
import com.vicious.viciouscore.client.gui.widgets.item.ImageWidgetSlot;
import com.vicious.viciouscore.client.textures.VCTextures;
import com.vicious.viciouscore.client.util.WindowGetter;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * Generic GUI used to create a default GUI system which means I can fabricate guis rather than create a gui image for every gui I'll need.
 */
public class GenericGUI<T extends GenericContainer<?>> extends AbstractContainerScreen<T> {
    protected RootWidget root = new RootWidget();
    protected int resizeX;
    protected int resizeY;
    protected int prevX = 0, prevY = 0;

    public GenericGUI(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        root.attachToMouse(new WidgetItem<>(root,-8,-8,16,16,()->menu.getInteractionState().getHeld()).onlyVisible());
        prevX = (int) (WindowGetter.window.getWidth()/2/WindowGetter.window.getGuiScale());
        prevY = (int) (WindowGetter.window.getHeight()/2/WindowGetter.window.getGuiScale());
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
        resizeX = (width/2 - prevX);
        resizeY = (height/2 - prevY);
        prevX = width/2;
        prevY = height/2;
        root.resize(resizeX,resizeY);
        super.resize(minecraft, width, height);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scroll) {
        root.widgetMouseOver().onScroll(scroll);
        return true;
    }

    protected void createHoloInv() {
        //Holoinvbackscreen.
        //WidgetFreeDrag holo = add(new WidgetFreeDrag(root,menu.playerSlots.get(0).x - 7,menu.playerSlots.get(0).y - 66, VCTextures.HOLOINVMKI.width(), VCTextures.HOLOINVMKI.height(), VCTextures.HOLOINVMKI.name()));
        WidgetFreeDrag<?> holo = add(new WidgetFreeDrag<>(root,100,100, VCTextures.HOLOINVMKI.width(), VCTextures.HOLOINVMKI.height(), VCTextures.HOLOINVMKI.name()));
        final int slotxspacing = 17;
        final int slotyspacing = 17;
        final int hotbarx = 6;
        final int hotbary = 65;


        for (int x = 0; x < 9; x++) {
            ImageWidgetSlot<?> s = new ImageWidgetSlot<>(root,hotbarx+slotxspacing*x,hotbary,18,18,VCTextures.SLOTHG.name(),menu.playerInv,x).setSelectedImage(VCTextures.SLOTHGS.name());
            //This is necessary because the background widget is 18x18 rather than 16x16
            s.item.translate(1,1);
            holo.addChild(s);
        }

        // Add the rest of the players inventory to the gui
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int slotNumber = 9 + y * 9 + x;
                int xpos = hotbarx + x * slotxspacing;
                int ypos = hotbary - 2 - (3-y) * slotyspacing;
                ImageWidgetSlot<?> s = new ImageWidgetSlot<>(root,xpos, ypos, 18,18,VCTextures.SLOTHG.name(),menu.playerInv,slotNumber).setSelectedImage(VCTextures.SLOTHGS.name());
                s.item.translate(1,1);
                holo.addChild(s);
            }
        }
    }
    protected <W extends VCWidget<?>> W add(W widget){
        root.addChild(widget);
        return widget;
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        root.render(matrixStack,mouseX,mouseY,partialTicks);
    }
}
