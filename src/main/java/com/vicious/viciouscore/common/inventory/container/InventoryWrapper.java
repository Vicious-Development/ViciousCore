package com.vicious.viciouscore.common.inventory.container;

import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Objects;

public class InventoryWrapper<T extends IFastItemHandler & IItemHandlerModifiable> {
    public int index = -1;
    public T inventory;
    public InventoryWrapper(T inventory){
        this.inventory=inventory;
    }
    public void dropItemInSlot(Player player, int slot, boolean dropAll){
        ItemStack slotStack = getItem(slot);
        if(slotStack.isEmpty()) return;
        ItemStack drop = slotStack.copy();
        if(!dropAll) drop.setCount(1);
        slotStack.shrink(drop.getCount());
        player.drop(drop, true);
    }

    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    public int getSize(){
        return inventory.getSlots();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryWrapper that = (InventoryWrapper) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }


    public ItemStack insert(ItemStack stack) {
        return inventory.insertItem(stack,false);
    }

    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot,stack);
    }

    public boolean mayPlace(int slot, ItemStack held) {
        return inventory.mayPlace(slot,held);
    }
}
