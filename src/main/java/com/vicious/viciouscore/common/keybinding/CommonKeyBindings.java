package com.vicious.viciouscore.common.keybinding;

import com.vicious.viciouscore.aunotamation.commonkeybinding.Mouse;
import com.vicious.viciouscore.common.util.SidedExecutor;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public static void setup(){
        Aunotamation.processObject(CommonKeyBindings.class);
    }
    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event){
        for (CommonKeyBinding key : keyBindingList.values()) {
            SidedExecutor.clientOnly(()-> event.register(key.toClientKeyBinding()));
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

    @SubscribeEvent
    public static void onKeyBindsRegistry(final RegisterKeyMappingsEvent event) {
        CommonKeyBindings.register(event);
    }

}
