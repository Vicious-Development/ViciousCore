package com.vicious.viciouscore.common.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import com.vicious.viciouscore.common.util.SidedExecutor;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class CommonKeyBinding {
    private static int nextId = -1;
    @OnlyIn(Dist.CLIENT)
    public KeyMapping clientKey;
    @OnlyIn(Dist.CLIENT)
    public IKeyConflictContext conflictContext;
    @OnlyIn(Dist.CLIENT)
    public KeyModifier modifier;

    public final String name;
    public final int defaultKeyCode;
    public String category;
    public boolean isDown = false;
    public final int ID;

    private boolean isMouse = false;

    public CommonKeyBinding(String name, int defaultKeyCode, String category){
        this.name=name;
        this.defaultKeyCode=defaultKeyCode;
        this.category=category;
        this.ID = nextId();
        SidedExecutor.clientOnly(()->{
            conflictContext = NoConflict.NOCONFLICT;
            modifier = KeyModifier.NONE;
        });
    }

    public CommonKeyBinding(String name, int defaultKeyCode, String category, int identifier){
        this.name=name;
        this.defaultKeyCode=defaultKeyCode;
        this.category=category;
        this.ID=identifier;
        SidedExecutor.clientOnly(()->{
            conflictContext = NoConflict.NOCONFLICT;
            modifier = KeyModifier.NONE;
        });
    }
    @OnlyIn(Dist.CLIENT)
    public KeyMapping toClientKeyBinding() {
        if(isMouse){
            clientKey = new KeyMapping(name, InputConstants.Type.MOUSE, defaultKeyCode, category);
        }
        else{
            clientKey = new KeyMapping(name, InputConstants.Type.KEYSYM, defaultKeyCode, category);
        }
        clientKey.setKeyConflictContext(conflictContext);
        clientKey.setKeyModifierAndCode(modifier,clientKey.getDefaultKey());
        return clientKey;
    }
    public static int nextId(){
        nextId++;
        return nextId;
    }
    public CommonKeyBinding copy(){
        return new CommonKeyBinding(name,defaultKeyCode,category,ID);
    }
    public String toString(){
        return "CKB: " + name + " : " + " : " + category + " : " + ID;
    }

    public void isMouse(boolean b) {
        isMouse = b;
    }
}
