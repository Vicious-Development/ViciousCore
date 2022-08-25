package com.vicious.viciouscore.common.data.state;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IDataTable<T> {
    IDataTable<T> supports(Function<T, Object> keyfunc, Class<?> keyClass);
    void add(T t);
    void remove(T t);
    boolean containsValue(T t);
    boolean containsKey(Object o);
    T get(Object o);
    void forEach(Consumer<T> consumer);
    int size();
}
