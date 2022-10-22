package com.vicious.viciouscore.common.data.state;

import com.vicious.viciouscore.common.inventory.SlotChangedEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IFastItemHandler extends IItemHandlerModifiable {
    boolean contains(ItemStack stack);

    ItemStack swap(int slot, ItemStack stack);

    ItemStack extractItem(ItemStack requested, boolean simulate);
    ItemStack insertItem(ItemStack push, boolean simulate);

    Collection<Integer> indexOf(ItemStack stack);

    void listenChanged(Consumer<SlotChangedEvent> cons);
    void stopListening(Consumer<SlotChangedEvent> cons);

    NonNullList<ItemStack> getItems();
    Container asContainer();

    boolean isEmpty();
    boolean mayPlace(int slot, ItemStack stack);

    Collection<Consumer<SlotChangedEvent>> getListeners();

    default void sendEvent(SlotChangedEvent event) {
        for (Consumer<SlotChangedEvent> listener : getListeners()) {
            listener.accept(event);
        }
    }
    default void sendEventPost(int slot, SlotChangedEvent.Action action){
        sendEvent(new SlotChangedEvent(getStackInSlot(slot), SlotChangedEvent.Phase.POST, action, slot,this));
    }
    default void sendEventPre(int slot, SlotChangedEvent.Action action){
        sendEvent(new SlotChangedEvent(getStackInSlot(slot), SlotChangedEvent.Phase.PRE, action, slot,this));
    }

    /**
     * Only returns a slot if the entire stack can fit in it.
     */
    default int getInsertableSlot(ItemStack stack){
        for (int i = 0; i < getSlots(); i++) {
            if(insertItem(i, stack, true).isEmpty()){
                return i;
            }
        }
        return -1;
    }

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
            if(other.insertItem(toInsert,true).isEmpty()){
                other.insertItem(extractItem(toInsert,false),false);
            }
        });
        return isEmpty();
    }

    default void forEachSlot(Consumer<Integer> cons){
        for (int i = 0; i < getSlots(); i++) {
            cons.accept(i);
        }
    }
    default Collection<ItemStack> matchAll(Predicate<ItemStack> tester){
        List<ItemStack> ret = new ArrayList<>();
        forEachSlot((i)->{
            ItemStack investigate = getStackInSlot(i);
            if(tester.test(investigate)){
                ret.add(investigate);
            }
        });
        return ret;
    }
    default boolean containsAtLeastOneOf(Predicate<ItemStack> tester){
        return matchAll(tester).size() > 0;
    }
}
