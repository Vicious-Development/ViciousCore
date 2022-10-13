package com.vicious.viciouscore.common.keybinding;

import com.vicious.viciouscore.aunotamation.commonkeybinding.Mouse;
import com.vicious.viciouscore.common.util.SidedExecutor;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraftforge.client.ClientRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to identify key bindings with an integer on both the client and server side.
 */
public class CommonKeyBindings {
    public static Map<Integer,CommonKeyBinding> keyBindingList = new HashMap<>();
    @Mouse
    public static CommonKeyBinding VCSHOOT = new CommonKeyBinding("key.vc.shoot", 0, "key.viciouscore.category");
    @Mouse
    public static CommonKeyBinding VCAIM = new CommonKeyBinding("key.vc.aim", 1, "key.viciouscore.category");
    public static void register(){
        Aunotamation.processObject(CommonKeyBindings.class);
        for (CommonKeyBinding key : keyBindingList.values()) {
            SidedExecutor.clientOnly(()-> ClientRegistry.registerKeyBinding(key.toClientKeyBinding()));
        }
    }

    public static CommonKeyBinding add(CommonKeyBinding in){
        keyBindingList.put(in.ID,in);
        return in;
    }

    public static Map<Integer,CommonKeyBinding> copyBindings() {
        Map<Integer,CommonKeyBinding> ret = new HashMap<>();
        keyBindingList.forEach((i,k)->{
            ret.put(k.ID,k.copy());
        });
        return ret;
    }
}
