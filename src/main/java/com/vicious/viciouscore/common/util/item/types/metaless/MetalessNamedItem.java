package com.vicious.viciouscore.common.util.item.types.metaless;


import com.vicious.viciouscore.common.util.item.types.ItemType;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * Ignores the full metadata, but keeps the name data.
 */
public class MetalessNamedItem extends TaglessItem<String> {
    public MetalessNamedItem(ItemStack stack){
        super(stack.getItem(),stack.hasTag() ? stack.getHoverName().getString() : "");
    }
    @Override
    public boolean isType(ItemType<?, ?> type, boolean ignoreMeta) {
        return Objects.equals(type.getType(),this.type) && Objects.equals(type.getType(),this.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type,meta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetalessNamedItem itemType = (MetalessNamedItem) o;
        return Objects.equals(type, itemType.type) && Objects.equals(meta,itemType.meta);
    }
}
