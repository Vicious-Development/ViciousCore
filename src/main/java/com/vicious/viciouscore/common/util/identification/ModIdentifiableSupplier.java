package com.vicious.viciouscore.common.util.identification;

import java.util.Objects;
import java.util.function.Supplier;

public class ModIdentifiableSupplier<T> implements Supplier<T>, IModIdentifiable {
    private final String modid;
    private final Supplier<T> supplier;
    public ModIdentifiableSupplier(Supplier<T> supplier, String modid){
        this.modid=modid;
        this.supplier=supplier;
    }

    @Override
    public String getModid() {
        return modid;
    }

    @Override
    public T get() {
        return supplier.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModIdentifiableSupplier<?> that = (ModIdentifiableSupplier<?>) o;
        return Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier);
    }
}
