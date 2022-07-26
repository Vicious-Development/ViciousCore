package com.vicious.viciouscore.common.network.packets.datasync;


import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class SPacketSyncData extends PacketSyncData{
    public SPacketSyncData(int targetID, CompoundTag tag) {
        super(targetID, tag);
    }

    public SPacketSyncData(FriendlyByteBuf buf) {
        super(buf);
    }

    public static class Window extends SPacketSyncData{
        public Window(int targetID, CompoundTag tag) {
            super(targetID, tag);
        }

        public Window(FriendlyByteBuf buf) {
            super(buf);
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            if(ctx.getSender() == null) return;
            AbstractContainerMenu target = ctx.getSender().containerMenu;
            if(target.containerId == getTargetID() && target instanceof GenericContainer<?> gc){
                gc.getData().deserializeNBT(getNBT(), DataAccessor.of(context.get().getSender()));
            }
        }
    }
}
