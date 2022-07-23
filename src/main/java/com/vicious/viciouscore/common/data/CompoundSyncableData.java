package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.datasync.CPacketSyncDataIDs;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class CompoundSyncableData {
    protected List<SyncableData> tosync = new ArrayList<>();
    public void updateClient(ServerPlayer player){
        for(int i = 0; i < tosync.size(); i++){
            tosync.get(i).updateClient(player);
        }
    }
    public void initializeClientWindow(int window, ServerPlayer plr){
        List<Integer> lst = new ArrayList<>();
        for (SyncableData syncableData : tosync) {
            lst.add(syncableData.instanceID);
        }
        VCNetwork.getInstance().sendToPlayer(plr,new CPacketSyncDataIDs.Window(lst,window));
    }
    public void putIntoNBT(CompoundTag nbt){
        for(SyncableData sync : tosync){
            sync.putIntoNBT(nbt);
        }
    }
    public void readFromNBT(CompoundTag nbt, DataEditor editor) {
        for(SyncableData sync : tosync){
            sync.readFromNBT(nbt, editor);
        }
    }
    public <T extends SyncableData> T add(T data){
        tosync.add(data);
        return data;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleCreationPacket(CPacketSyncDataIDs.Window pkt) {
        IntList ints = pkt.instanceIDs;
        for (int i = 0; i < ints.size(); i++) {
            tosync.get(i).instanceID=ints.getInt(i);
        }
    }
}
