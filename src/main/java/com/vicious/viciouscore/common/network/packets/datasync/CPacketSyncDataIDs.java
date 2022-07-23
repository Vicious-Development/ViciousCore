package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import com.vicious.viciouscore.common.network.VCPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public abstract class CPacketSyncDataIDs extends VCPacket {
    public final IntList instanceIDs;
    public CPacketSyncDataIDs(List<Integer> instances){
        instanceIDs = new IntArrayList(instances);
    }
    public CPacketSyncDataIDs(FriendlyByteBuf buf){
        instanceIDs = buf.readIntIdList();
    }
    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeIntIdList(instanceIDs);
    }
    public static class Window extends CPacketSyncDataIDs {
        public final int window;
        public Window(List<Integer> instances, int window){
            super(instances);
            this.window=window;
        }
        public Window(FriendlyByteBuf buf){
            super(buf);
            this.window=buf.readInt();
        }
        @Override
        public void handle(Supplier<NetworkEvent.Context> context) {
            if(Minecraft.getInstance().player != null) {
                AbstractContainerMenu openMenu = Minecraft.getInstance().player.containerMenu;
                if (openMenu.containerId == window) {
                    if (openMenu instanceof GenericContainer<?> container) {
                        container.getCompoundData().handleCreationPacket(this);
                    }
                }
            }
        }
    }
}
