package com.vicious.viciouscore.client.network;

import com.drathonix.zeroenergy.common.containers.GenericContainer;
import com.drathonix.zeroenergy.common.network.ZENetwork;
import com.drathonix.zeroenergy.common.network.packets.SSyncContainerStateData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;

public abstract class ZEClientNetplayHandler implements IClientPlayNetHandler {
    public static void handleStateChange(SSyncContainerStateData packetIn) {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        if (playerentity.openContainer != null && playerentity.openContainer.windowId == packetIn.getWindowId()) {
            ((GenericContainer)playerentity.openContainer).handleStateChange(packetIn.getNBT());
        }
    }

    public static boolean protocolSupported(String protocolVer) {
        return ZENetwork.PROTOCOLVER.equals(protocolVer);
    }
}
