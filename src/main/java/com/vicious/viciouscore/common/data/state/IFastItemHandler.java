package com.vicious.viciouscore.common.data.state;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IFastItemHandler extends IItemHandlerModifiable {
    boolean contains(ItemStack stack);
    ItemStack extractItem(ItemStack requested, boolean simulate);
    ItemStack insertItem(ItemStack push, boolean simulate);
    ItemStack forceInsertItem(ItemStack push, boolean simulate);

    @NotNull ItemStack forceInsertItem(int slot, @NotNull ItemStack stack, boolean simulate);
    void listenChanged(Consumer<IFastItemHandler> cons);
    void stopListening(Consumer<IFastItemHandler> cons);
    void onUpdate();
    boolean isEmpty();
    boolean mayPlace(int slot, ItemStack stack);

    default boolean emptyInto(IFastItemHandler other) {
        if(isEmpty()) return true;
        forEachSlot((i)->{
            ItemStack toInsert = getStackInSlot(i);
            if(other.insertItem(toInsert,true).isEmpty()){
                other.insertItem(extractItem(toInsert,false),false);
            }
        });
        return isEmpty();
    }

    default boolean forceEmptyInto(IFastItemHandler other) {
        if(isEmpty()) return true;
        forEachSlot((i)->{
            ItemStack toInsert = getStackInSlot(i);
            if(other.forceInsertItem(toInsert,true).isEmpty()){
                other.forceInsertItem(extractItem(toInsert,false),false);
            }
        });
        return isEmpty();
    }

    default void forEachSlot(Consumer<Integer> cons){
        for (int i = 0; i < getSlots(); i++) {
            cons.accept(i);
        }
    }
}
