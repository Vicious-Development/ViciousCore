package com.vicious.viciouscore.client.gui.widgets;


import com.vicious.viciouscore.client.util.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WidgetButton extends WidgetInteraction {
    private final int ID;
    private final BlockPos TILEPOS;
    private boolean doClick = true;
    public WidgetButton(int x, int y, int w, int h, TextComponent text, ResourceLocation source, int id, BlockPos pos) {
        super(x, y, w, h, text, source);
        ID=id;
        TILEPOS=pos;
    }

    public WidgetButton(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation source, int id, BlockPos pos) {
        super(x, y, w, h, tv, text, source);
        ID=id;
        TILEPOS =pos;
    }
    public WidgetButton getUpdatedWidget() {
        WidgetButton updated = new WidgetButton(x, y, width, height, startPos, getMessage(), source, ID, TILEPOS);
        updated.children = updateChildren();
        return updated;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(!doClick){
            doClick = true;
            return;
        }
        this.playDownSound(Minecraft.getInstance().getSoundHandler());
        ZENetwork.simplechannel.sendToServer(new CClickContainerButton(TILEPOS,ID));
        doClick = false;
    }
}
