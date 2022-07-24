package com.vicious.viciouscore.common.data;


import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.data.values.Syncable;
import com.vicious.viciouscore.common.data.values.SyncableValue;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.packets.datasync.SPacketSyncData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public abstract class SyncableData implements IVCCapabilityHandler {
    protected List<Syncable<?>> tosync = new ArrayList<>();

    public void track(Syncable<?> totrack){
        tosync.add(totrack);
    }
    public void updateClient(ServerPlayer player, int window){
        CompoundTag nbt = new CompoundTag();
        for (Syncable<?> syncable : tosync) {
            if(syncable.clientReadable()) syncable.putIntoNBT(nbt);
        }
        if(!nbt.isEmpty()) VCNetwork.getInstance().sendToPlayer(player,new SPacketSyncData.Window(window,nbt));
    }
    public void putIntoNBT(CompoundTag nbt){
        for(Syncable<?> sync : tosync){
            if(!sync.doSave) continue;
            sync.putIntoNBT(nbt);
        }
    }
    public void readFromNBT(CompoundTag nbt, DataEditor editor) {
        for(Syncable<?> sync : tosync){
            if (!sync.doSave) continue;
            if (sync instanceof SyncableValue) {
                if (nbt.contains(((SyncableValue<?>) sync).name)) {
                    if (editor.isRemoteEditor() || sync.clientEditable()) {
                        sync.readFromNBT(nbt);
                    }
                    else{
                        editor.securityViolated("They have attempted to modify an unmodifiable value! This is an indicator of attempting to find exploits!");
                    }
                }
            }
        }
    }
    public abstract List<Capability<?>> getCapabilityTokens();
}
