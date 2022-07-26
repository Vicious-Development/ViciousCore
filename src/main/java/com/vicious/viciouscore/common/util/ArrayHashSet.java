package com.vicious.viciouscore.common.util;

import java.util.ArrayList;
import java.util.HashSet;

//I need this because get(i) in linkedlist is slower.
public class ArrayHashSet<T> extends HashSet<T> {
    private final ArrayList<T> list = new ArrayList<>();
    public T get(int index){
        return list.get(index);
    }
    public ArrayHashSet(){}

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
}
