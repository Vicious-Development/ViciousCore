package com.vicious.viciouscore.client.util;

import net.minecraft.client.MainWindow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class WindowGetter {
    public static MainWindow window;
    public static int initialHeight = -1;
    public static int initialWidth = -1;
    private static boolean initialized = false;

    @SubscribeEvent
    public static void renderGameOverlay(RenderGameOverlayEvent.Post e) {
        window = e.getWindow();
        if (!initialized) {
            initialHeight = window.getHeight();
            initialWidth = window.getWidth();
            initialized = true;
        }
    }
}
