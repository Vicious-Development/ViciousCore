package com.vicious.viciouscore.client.util;

import com.mojang.blaze3d.platform.Window;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class WindowGetter {
    public static Window window;
    public static int initialHeight = -1;
    public static int initialWidth = -1;
    public static boolean initialized = false;

    @SubscribeEvent
    public static void renderGameOverlay(RenderGuiOverlayEvent.Post e) {
        window = e.getWindow();
        if (!initialized) {
            initialHeight = window.getHeight();
            initialWidth = window.getWidth();
            initialized = true;
        }
    }
}
