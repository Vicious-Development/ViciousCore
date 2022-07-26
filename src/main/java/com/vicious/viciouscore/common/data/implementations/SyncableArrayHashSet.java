package com.vicious.viciouscore.common.data.implementations;

import com.vicious.viciouscore.common.data.structures.SyncableINBTCompound;
import com.vicious.viciouscore.common.util.ArrayHashSet;
import net.minecraft.nbt.Tag;

import java.util.function.Function;

public class SyncableArrayHashSet<T> extends SyncableINBTCompound<ArrayHashSet<T>> {
    public SyncableArrayHashSet(String key, Function<Tag,T> deserializer) {
        super(key, new ArrayHashSet<T>(deserializer));
    }
}
