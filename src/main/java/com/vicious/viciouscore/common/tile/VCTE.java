package com.vicious.viciouscore.common.tile;

import com.vicious.viciouscore.common.data.CompoundSyncableData;
import com.vicious.viciouscore.common.data.DataEditor;
import com.vicious.viciouscore.common.data.SyncableData;
import com.vicious.viciouscore.common.util.FuckLazyOptionals;
import com.vicious.viciouscore.common.util.VCMath;
import com.vicious.viciouslib.util.TriConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VCTE extends BlockEntity  {
    protected final CompoundSyncableData data = new CompoundSyncableData();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return data.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return data.getCapability(cap,side);
    }

    public VCTE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }

    @Override
    public void load(CompoundTag tag) {
        data.readFromNBT(tag,DataEditor.LOCAL);
    }

    public List<BlockEntity> getNeighborBEntitys(){
        List<BlockEntity> list = new ArrayList<>();
        if(level == null) return list;
        for (Direction dir : Direction.values()) {
            BlockEntity ent = level.getBlockEntity(worldPosition.offset(dir.getStepX(),dir.getStepY(),dir.getStepZ()));
            if(ent != null) list.add(ent);
        }
        return list;
    }
    
    public List<BlockState> getNeighborBlocks() {
        List<BlockState> list = new ArrayList<>();
        if(level == null) return list;
        for (Direction dir : Direction.values()) {
            list.add(level.getBlockState(worldPosition.offset(dir.getStepX(),dir.getStepY(),dir.getStepZ())));
        }
        return list;
    }
    public void forNeighborBlockTiles(TriConsumer<BlockState,BlockEntity,BlockPos> consumer){
        if(level == null) return;
        for (Direction dir : Direction.values()) {
            BlockPos p = worldPosition.offset(dir.getStepX(),dir.getStepY(),dir.getStepZ());
            consumer.accept(level.getBlockState(p),level.getBlockEntity(p),p);
        }
    }
    public <T> List<T> getNeighborCapabilities(Capability<T> token){
        List<T> caps = new ArrayList<>();
        if(level == null) return caps;
        for (Direction dir : Direction.values()) {
            BlockPos adjacent = worldPosition.offset(dir.getStepX(),dir.getStepY(),dir.getStepZ());
            BlockEntity adjaBE = level.getBlockEntity(adjacent);
            if(adjaBE != null) {
                T cap = FuckLazyOptionals.getOrNull(adjaBE.getCapability(token,dir.getOpposite()));
                if(cap != null) caps.add(cap);
            }
        }
        return caps;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        data.readFromNBT(nbt, DataEditor.LOCAL);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag supe = super.serializeNBT();
        data.putIntoNBT(supe);
        return supe;
    }

    public boolean isWithinAccessRange(Player player) {
        return VCMath.getDistance(player.position(),new Vec3(worldPosition.getX()+0.5,worldPosition.getY()+0.5,worldPosition.getZ()+0.5)) < 8.0;
    }

    public <T extends SyncableData> T addSyncable(T sync){
        return data.put(sync);
    }
    public void tick(){}
}
