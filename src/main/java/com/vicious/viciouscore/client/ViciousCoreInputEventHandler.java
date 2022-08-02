package com.vicious.viciouscore.client;


import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;
import com.vicious.viciouscore.common.network.packets.keybindpress.CPacketButtonPressReceived;
import com.vicious.viciouscore.common.network.packets.keybindpress.SPacketButtonUpdate;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ViciousCoreInputEventHandler {
    @SubscribeEvent
    public static void onButtonPressed(TickEvent.ClientTickEvent ev){
        if(ev.phase == TickEvent.Phase.START) return;
        CPacketButtonPressReceived.Handler handler = CPacketButtonPressReceived.Handler.getInstance();
        CommonKeyBindings.keyBindingList.forEach((i, k)->{
            KeyMapping binding = k.clientKey;
            if (binding.isDown()) {
                if (!k.isDown) {
                    handler.startSending(new SPacketButtonUpdate(k.ID, true));
                }
                k.isDown = true;
            } else {
                if (k.isDown) {
                    handler.startSending(new SPacketButtonUpdate(k.ID, false));
                }
                k.isDown = false;
            }
        });
        handler.tick();
    }
}
