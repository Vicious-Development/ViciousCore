package com.vicious.viciouscore.client.render;

import codechicken.lib.render.CCRenderState;
import com.vicious.viciouscore.client.render.ViciousRenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public interface ICCModelUser {
    default CCRenderState startAndBind(ResourceLocation texture){
        CCRenderState rs = CCRenderState.instance();
        rs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        ViciousRenderManager.bindTexture(texture);
        return rs;
    }
    default void draw(CCRenderState rs){
        rs.draw();
    }
}
