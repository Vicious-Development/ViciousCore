package com.vicious.viciouscore.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiBase extends GuiContainer {
    public Container container;
    public TileEntity tileEntity;
    public EntityPlayer player;
    public ResourceLocation overlay;
    public ResourceLocation texture;
    public String name;

    public GuiBase(Container container, TileEntity tileEntity, EntityPlayer entityPlayer, ResourceLocation overlay, ResourceLocation texture, String name) {
        super(container);
        this.container = container;
        this.tileEntity = tileEntity;
        this.player = entityPlayer;
        this.overlay = overlay;
        this.texture = texture;
        this.name = name;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = "tile." + this.name + ".name";
        this.fontRenderer.drawString(name, this.xSize / 2 - 6 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(texture);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0 ,0, this.xSize, this.ySize);
    }
}
