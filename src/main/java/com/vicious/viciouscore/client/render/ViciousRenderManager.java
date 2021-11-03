package com.vicious.viciouscore.client.render;

import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unchecked")
public class ViciousRenderManager implements ITickable {
    public static int tickTimeElasped = 0;
    public static ViciousRenderManager instance;
    private LinkedList<ViciousRender> renders = new LinkedList<>();
    public ViciousRenderManager(){
        instance = this;
        //Force my way into minecraft's texture ticking system to allow my renders to tick without me needing to tick it myself >:) IM SO EVIL MUAHAHAHA
        //Disabled for now since I don't even use it lmao
        //((List<ITickable>) Reflection.accessField(Minecraft.getMinecraft().getTextureManager(),"listTickables")).add(this);
    }

    @Override
    public void tick() {
        //We do this because its more efficient to separate ViciousRenders from the minecraft render ticker as these are intended to be removed from ticking.
        Iterator<ViciousRender> ite = renders.iterator();
        while(ite.hasNext()){
            ViciousRender render = ite.next();
            if(render.isExpired()){
                ite.remove();
            }
            else render.tick();
        }
        tickTimeElasped++;
    }
    //Syntatic sweetness
    public static void bindTexture(ResourceLocation rl){
        Minecraft.getMinecraft().renderEngine.bindTexture(rl);
    }
    public ViciousRender addRender(ViciousRender render){
        renders.add(render);
        return render;
    }
    public static float ticks(float pt){
        return tickTimeElasped+pt;
    }

    //Gets the correct lighting value for an object at the position.
    public static float getLightingBrightness(BlockPos pos){
        return Minecraft.getMinecraft().world.getLightBrightness(pos)*200;
    }
}
