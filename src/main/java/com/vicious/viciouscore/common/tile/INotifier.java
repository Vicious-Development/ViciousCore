package com.vicious.viciouscore.common.tile;

import java.util.List;

public interface INotifier<T> {
    void notifyParent();

    void addParent(INotifiable<T> parent);
    List<INotifiable<T>> getParents();

    void setParents(List<INotifiable<Object>> parents);
}
