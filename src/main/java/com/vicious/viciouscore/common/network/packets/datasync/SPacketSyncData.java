package com.vicious.viciouscore.common.network.packets.datasync;


import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class SPacketSyncData<T extends SyncTarget> extends PacketSyncData<T>{
    public SPacketSyncData(T target, CompoundTag tag) {
        super(target, tag);
    }
    public SPacketSyncData(T target, FriendlyByteBuf buf){
        super(target,buf);
    }


    public static class Window extends SPacketSyncData<SyncTarget.Window>{
        public Window(SyncTarget.Window target, CompoundTag tag) {
            super(target,tag);
        }

        public Window(FriendlyByteBuf buf) {
            super(new SyncTarget.Window(null),buf);
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            if(ctx.getSender() == null) return;
            AbstractContainerMenu target = ctx.getSender().containerMenu;
            if(target instanceof GenericContainer<?> gc){
                gc.getData().deserializeNBT(getNBT(), DataAccessor.of(context.get().getSender()));
            }
        }
    }
}

