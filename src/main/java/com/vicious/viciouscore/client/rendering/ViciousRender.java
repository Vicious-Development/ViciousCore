package com.vicious.viciouscore.client.rendering;


import net.minecraft.client.renderer.texture.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Anything renderable that doesn't depend on objects in game. (Can be rendered anywhere at anytime)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unchecked")
public abstract class ViciousRender implements ITickable {
    protected int expiry = 0;
    protected int framesExisted = 0;
    public ViciousRender(int expiry){
        this.expiry=expiry;
    }
    public void tick(){
        framesExisted++;
    }
    public boolean isExpired(){
        return framesExisted >= expiry;
    }
}
