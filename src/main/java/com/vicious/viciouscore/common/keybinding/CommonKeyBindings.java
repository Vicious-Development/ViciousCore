package com.vicious.viciouscore.common.keybinding;

import com.vicious.viciouscore.common.util.SidedExecutor;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to identity key bindings with an integer on both the client and server side.
 */
public class CommonKeyBindings {
    public static Map<Integer,CommonKeyBinding> keyBindingList = new HashMap<>();
    public static CommonKeyBinding VCSHOOT = add(new CommonKeyBinding("key.vc.shoot", -99, "key.viciouscore.category"));
    public static CommonKeyBinding VCAIM = add(new CommonKeyBinding("key.vc.aim", -100, "key.viciouscore.category"));
    public static void setup(){}
    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event){
        for (CommonKeyBinding key : keyBindingList.values()) {
            SidedExecutor.clientOnly(()-> event.register(key.toClientKeyBinding(KeyConflictContext.IN_GAME,null)));
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
