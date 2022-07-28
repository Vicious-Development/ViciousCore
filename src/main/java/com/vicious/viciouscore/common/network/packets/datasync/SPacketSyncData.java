package com.vicious.viciouscore.common.network.packets.datasync;


import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.inventory.container.GenericContainer;
import com.vicious.viciouscore.common.tile.VCTE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class SPacketSyncData<T extends SyncTarget> extends PacketSyncData<T>{
    public SPacketSyncData(T target, CompoundTag tag) {
        super(target, tag);
    }

    public SPacketSyncData(T target,FriendlyByteBuf buf) {
        super(target,buf);
    }
    

    public static class Window extends SPacketSyncData<SyncTarget.Window>{
        public Window(SyncTarget.Window target, CompoundTag tag) {
            super(target,tag);
        }

        public Window(FriendlyByteBuf buf) {
            super(new SyncTarget.Window(null,buf.readInt()),buf);
        }

        @Override
        public void handle(Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            if(ctx.getSender() == null) return;
            AbstractContainerMenu target = ctx.getSender().containerMenu;
            if(target.containerId == getTarget().window && target instanceof GenericContainer<?> gc){
                gc.getData().deserializeNBT(getNBT(), DataAccessor.of(context.get().getSender()));
            }
        }
    }
    public static class Tile extends SPacketSyncData<SyncTarget.Tile> {
        public Tile(SyncTarget.Tile target, CompoundTag tag) {
            super(target, tag);
        }

        public Tile(FriendlyByteBuf buf) {
            super(new SyncTarget.Tile(null,buf.readBlockPos()),buf);
        }
        @Override
        @SuppressWarnings("all")
        public void handle(Supplier<NetworkEvent.Context> context) {
            NetworkEvent.Context ctx = context.get();
            if(ctx.getSender() == null) return;
            ServerPlayer sender = ctx.getSender();
            BlockEntity be = sender.level.getBlockEntity(getTarget().position);
            if(be instanceof VCTE vcte){
                vcte.getData().deserializeNBT(getNBT(),DataAccessor.of(sender));
            }
        }
    }}
