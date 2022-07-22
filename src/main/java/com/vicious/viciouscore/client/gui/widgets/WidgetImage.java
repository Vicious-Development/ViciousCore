package com.vicious.viciouscore.client.gui.widgets;

import com.vicious.viciouscore.client.util.Vector2i;
import com.vicious.viciouscore.client.util.WindowGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WidgetImage extends VCWidget {
    private static Minecraft minecraft = Minecraft.getInstance();

    //Used to control what part of the image is shown.
    public int cutOffRight = 0, cutOffLeft = 0, cutOffTop = 0, cutOffBottom = 0;
    public ResourceLocation source;
    public WidgetImage(int x, int y, int w, int h, TextComponent text, ResourceLocation widgetResource) {
        super(x, y, w, h, text);
        source = widgetResource;
    }
    public WidgetImage(int x, int y, int w, int h, Vector2i tv, TextComponent text, ResourceLocation widgetResource) {
        super(x, y, w, h, tv, text);
        source = widgetResource;

    }
    //Renders four widget corners accounting for horizontal and vertical cutoff masking.
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(WindowGetter.window == null || !visible) return;
        MainWindow window = WindowGetter.window;
        minecraft.getTextureManager().bindTexture(source);
        boolean doRender = true;
        boolean DRL = true;
        boolean DRR = true;
        boolean DRU = true;
        boolean DRD = true;
        int uvy = -(this.getYImage(this.isHovered())) + 1;
        int uvx = 0;
        int newX = x;// + relativisticPos.getActualX();
        int newY = y;// + relativisticPos.getActualY();
        int width = getWidth();
        int height = getHeight();
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

        //TODO:Implement bottom-up rendering.
        if(doRender) {
            //Left Top
            if(DRU && DRL) blit(matrixStack, newX, newY, uvx-uvxshift, uvy-uvyshift, halfWidthLeft, halfHeightTop, width, height);
            //Left Bottom
            if(DRD && DRL) blit(matrixStack, newX, newY + halfHeightTop, uvx-uvxshift, uvy - halfHeightBottom, halfWidthLeft, halfHeightBottom, width, height);
            //Right Top
            if(DRU && DRR) blit(matrixStack, newX + halfWidthLeft, newY, width - halfWidthLeft, uvy-uvyshift, halfWidthRight, halfHeightTop, width, height);
            //Right Bottom
            if(DRD && DRR) blit(matrixStack, newX + halfWidthLeft, newY + halfHeightTop, width - halfWidthLeft, uvy - halfHeightBottom, halfWidthRight, halfHeightBottom, width, height);
        }
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
    }
    public WidgetImage getUpdatedWidget(){
        WidgetImage updated = new WidgetImage(x, y, width, height, startPos, getMessage(), source);
        updated.children = updateChildren();
        return updated;
    }
    protected int getYImage(boolean isHovered) {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (isHovered && !(this instanceof WidgetDraggable)) {
            i = 2;
        }

        return i;
    }
}

