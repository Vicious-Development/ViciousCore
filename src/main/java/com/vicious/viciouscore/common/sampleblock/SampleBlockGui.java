package com.vicious.viciouscore.common.sampleblock;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SampleBlockGui extends GuiContainer {
    public static int value = 0;
    private static final ResourceLocation TEXTURES = new ResourceLocation(ViciousCore.MODID + ":textures/gui/sampleblockgui.png");
    private final InventoryPlayer player;
    private final TileEntitySampleBlock tileEntity;

    public SampleBlockGui(InventoryPlayer player, TileEntitySampleBlock tileEntity)
    {
        super(new ContainerSampleBlock(player, tileEntity));
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        ITextComponent tileDisplayName = this.tileEntity.getDisplayName();
        assert tileDisplayName != null;
        String tileName = tileDisplayName.getUnformattedText();
        this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3, 8, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if(TileEntitySampleBlock.isActive(tileEntity))
        {
            int k = this.getBurnLeftScaled();
            this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 54 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.getProcessProgressScaled();
        this.drawTexturedModalRect(this.guiLeft + 44, this.guiTop + 36, 176, 14, l + 1, 16);
    }

    private int getBurnLeftScaled()
    {
        int i = this.tileEntity.getField(1);
        if(i == 0) i = 200;
        return this.tileEntity.getField(0) * 13 / i;
    }

    private int getProcessProgressScaled()
    {
        int i = this.tileEntity.getField(2);
        int j = this.tileEntity.getField(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}
