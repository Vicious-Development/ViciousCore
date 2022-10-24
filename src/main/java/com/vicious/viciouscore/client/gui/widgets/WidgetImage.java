package com.vicious.viciouscore.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vicious.viciouscore.client.util.WindowGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class WidgetImage<T extends WidgetImage<T>> extends VCWidget<T> {
    private static Minecraft minecraft = Minecraft.getInstance();

    //Used to control what part of the image is shown.
    public int cutOffRight = 0, cutOffLeft = 0, cutOffTop = 0, cutOffBottom = 0;
    public ResourceLocation source;
    public WidgetImage(RootWidget root, int x, int y, int w, int h, ResourceLocation widgetResource) {
        super(root, x, y, w, h);
        source = widgetResource;
    }

    @Override
    public void renderWidget(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(WindowGetter.window == null) return;
        RenderSystem.setShaderTexture(0,source);
        RenderSystem.enableBlend();
        boolean doRender = true;
        boolean DRL = true;
        boolean DRR = true;
        boolean DRU = true;
        boolean DRD = true;
        int uvy = -(this.getYImage(hasFlag(ControlFlag.HOVERED))) + 1;
        int uvx = 0;
        int newX = actualPosition.x;
        int newY = actualPosition.y;
        int width = actualWH.x;
        int height = actualWH.y;
        int halfWidthLeft = width / 2;
        int halfWidthRight = width % 2 == 0 ? halfWidthLeft : halfWidthLeft + 1;
        int halfHeightTop = height / 2;
        int halfHeightBottom = height % 2 == 0 ? halfHeightTop : halfHeightTop + 1;
        int uvyshift = cutOffTop != 0 ? halfHeightBottom+(halfHeightTop-cutOffTop) : 0;
        int uvxshift = cutOffLeft != 0 ? halfWidthRight + (halfWidthLeft-cutOffLeft) : 0;
        //Cutoff checks and Calculations right side
        if(cutOffRight == 0);
        else if(cutOffRight < halfWidthRight){
            halfWidthRight -= cutOffRight;
        }
        else if(cutOffRight == halfWidthRight) DRR = false;
        else if(cutOffRight < width){
            DRR = false;
            halfWidthLeft -= cutOffRight-halfWidthRight;
        }
        else doRender = false;

        //These determine what parts of the image will be rendered.
        //Cutoff checks and Calculations left side
        if(doRender){
            if(cutOffLeft == 0);
            else if(cutOffLeft < halfWidthLeft){
                halfWidthLeft -= cutOffLeft;
                newX+=cutOffLeft;
            }
            else if(cutOffLeft == halfWidthLeft) DRL = false;
            else if(cutOffLeft < width){
                DRL = false;
                newX+=cutOffLeft-halfWidthRight;
                halfWidthRight -= cutOffLeft-halfWidthLeft;
            }
            else doRender = false;
            //Does not render if either side won't render.
            doRender = DRR || DRL;
        }
        //Cutoff checks and Calculations top side
        if(doRender){
            if(cutOffTop == 0);
            else if (cutOffTop < halfHeightTop) {
                halfHeightTop -= cutOffTop;
                newY+=cutOffTop;
            }
            else if (cutOffTop == halfHeightTop) DRU = false;
            else if (cutOffTop < height) {
                DRU = false;
                newY+=cutOffTop-halfHeightBottom;
                halfHeightBottom -= cutOffTop - halfHeightBottom;
            } else doRender = false;

            //Does not render if either side won't render.
        }
        //Cutoff checks and Calculations bottom side
        if(doRender){
            if(cutOffBottom == 0);
            else if(cutOffBottom < halfHeightBottom){
                halfHeightBottom -= cutOffBottom;
            }
            else if(cutOffBottom == halfHeightBottom) DRD = false;
            else if(cutOffBottom < height){
                DRD = false;
                halfHeightTop -= cutOffBottom-halfHeightBottom;
            }
            else doRender = false;
            //Does not render if either side won't render.
            doRender = DRD || DRU;
        }


        //Calculating the top half with cut off.
        //Height calcs remain the same,
        //Position is shifted downwards by the cutoff.
        //Image shown must also be the bottom, however the renderer renders top down meaning the top is shown rather than the bottom
        //Solution: shift the rendering start position down

        if(doRender) {
            //Left Top
            if(DRU && DRL) Screen.blit(matrixStack, newX, newY, uvx - uvxshift, uvy - uvyshift, halfWidthLeft, halfHeightTop, width, height);
            //Left Bottom
            if(DRD && DRL) Screen.blit(matrixStack, newX, newY + halfHeightTop, uvx-uvxshift, uvy - halfHeightBottom, halfWidthLeft, halfHeightBottom, width, height);
            //Right Top
            if(DRU && DRR) Screen.blit(matrixStack, newX + halfWidthLeft, newY, width - halfWidthLeft, uvy-uvyshift, halfWidthRight, halfHeightTop, width, height);
            //Right Bottom
            if(DRD && DRR) Screen.blit(matrixStack, newX + halfWidthLeft, newY + halfHeightTop, width - halfWidthLeft, uvy - halfHeightBottom, halfWidthRight, halfHeightBottom, width, height);
        }
        RenderSystem.disableBlend();
        super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
    }
    protected int getYImage(boolean isHovered) {
        if(hasFlag(ControlFlag.RESPONDTOHOVER)) {
            if (isHovered) {
                return 2;
            }
        }
        return 1;
    }
    private void resetColor(){
        RenderSystem.setShaderColor(1,1,1,1);
    }
}

