package com.vicious.viciouscore.common.network.packets.datasync;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class CPacketSyncData<T extends SyncTarget> extends PacketSyncData<T>{

    public CPacketSyncData(T target,FriendlyByteBuf buf){
        super(target,buf);
    }
    public CPacketSyncData(T target, CompoundTag tag) {
        super(target, tag);
    }

    @Override
    public boolean handleOnClient() {
        return true;
    }

    public static class Window extends CPacketSyncData<SyncTarget.Window>{
        public Window(SyncTarget.Window target, CompoundTag tag) {
            super(target, tag);
        }

        public Window(FriendlyByteBuf buf) {
            super(new SyncTarget.Window(DataAccessor.LOCAL),buf);
        }

        @Override
        @SuppressWarnings("all")
        public void handle(Supplier<NetworkEvent.Context> context) {
            AbstractContainerMenu target = Minecraft.getInstance().player.containerMenu;
            if (target instanceof GenericContainer<?> gc) {
                gc.getData().deserializeNBT(getNBT(), DataAccessor.FORCEREMOTE);
            }
        }
    }
}
