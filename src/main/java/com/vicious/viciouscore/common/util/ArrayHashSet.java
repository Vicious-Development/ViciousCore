package com.vicious.viciouscore.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Function;

//I need this because get(i) in linkedlist is slower.
public class ArrayHashSet<T> extends HashSet<T> implements INBTSerializable<CompoundTag> {
    private final ArrayList<T> list = new ArrayList<>();
    private Function<Tag,T> nbtDeserializer;
    public T get(int index){
        return list.get(index);
    }
    public ArrayHashSet(){}
    public ArrayHashSet(Function<Tag,T> nbtDeserializer){
        this.nbtDeserializer=nbtDeserializer;
    }

    @Override
    public boolean add(T t) {
        if(super.add(t)){
            list.add(t);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        boolean rem = super.remove(o);
        if(rem) list.remove(o);
        return rem;
    }

    public boolean remove(int index) {
        boolean rem = super.remove(list.get(index));
        if(rem) list.remove(index);
        return rem;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if(t instanceof INBTSerializable<?> nbt) {
                tag.put(""+i, nbt.serializeNBT());
            }
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbtDeserializer == null) return;
        for (String key : nbt.getAllKeys()) {
            add(nbtDeserializer.apply(nbt.get(key)));
        }
    }
}
