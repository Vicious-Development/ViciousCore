package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.data.DataEditor;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class CPacketSyncData extends PacketSyncData{

    public CPacketSyncData(int targetID, CompoundTag tag) {
        super(targetID, tag);
    }

    public CPacketSyncData(FriendlyByteBuf buf) {
        super(buf);
    }

    public static class Window extends CPacketSyncData{
        public Window(int targetID, CompoundTag tag) {
            super(targetID, tag);
        }

        public Window(FriendlyByteBuf buf) {
            super(buf);
        }

        @Override
        @SuppressWarnings("all")
        public void handle(Supplier<NetworkEvent.Context> context) {
            AbstractContainerMenu target = Minecraft.getInstance().player.containerMenu;
            if (target.containerId == getTargetID() && target instanceof GenericContainer<?> gc) {
                gc.getCompoundData().readFromNBT(getNBT(), DataEditor.LOCAL);
            }
        }
    }
}
