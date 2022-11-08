package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.Obscured;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableGlobalData;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.util.server.ServerHelper;
import com.vicious.viciouslib.aunotamation.Aunotamation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class GlobalData extends SavedData implements ISyncableCompoundHolder {
    @Obscured
    public SyncableGlobalData globalData = new SyncableGlobalData();

    public static SyncableGlobalData getGlobalData(){
        return get(ServerHelper.server).globalData;
    }

    private GlobalData(){
        Aunotamation.processObject(globalData);
    }

    @Override
    public boolean isDirty() {
        return globalData.changed();
    }

    private static GlobalData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(GlobalData::load,GlobalData::new,"viciouscoreglobaldata");
    }

    private static GlobalData load(CompoundTag compoundTag) {
        GlobalData data = new GlobalData();
        data.globalData.deserializeNBT(compoundTag, DataAccessor.WORLD);
        return data;
    }


    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        globalData.serializeNBT(tag, DataAccessor.WORLD);
        return tag;
    }

    @Override
    public SyncableCompound getData() {
        return globalData;
    }
}
