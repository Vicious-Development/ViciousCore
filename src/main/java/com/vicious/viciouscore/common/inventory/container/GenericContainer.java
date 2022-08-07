package com.vicious.viciouscore.common.inventory.container;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.state.IFastItemHandler;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotInteraction;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotKeyPressed;
import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericContainer<T extends ISyncableCompoundHolder> extends AbstractContainerMenu implements ISyncableCompoundHolder{
    public static final RangedInteger PLAYER_MAIN_INVENTORY = new RangedInteger(0, 36);
    protected List<InventoryWrapper<?>> inventories = new ArrayList<>();
    public InventoryWrapper<?> playerInv;
    public Player plr;
    protected UserInteractionState interaction = new UserInteractionState();
    public final T target;


    public InventoryWrapper<?> getInventory(int index){
        return inventories.get(index);
    }
    /**
     * Server End
     */
    public GenericContainer(@Nullable MenuType<?> type, int windowId, Inventory playerInv, T target) {
        super(type, windowId);
        this.playerInv = newSlotList(playerInv);
        this.plr=playerInv.player;
        this.target = target;
        getData().listenChanged(this::onDataChange);
    }

    /**
     * Client End
     */
    public GenericContainer(@Nullable MenuType<?> type, int windowId, Inventory playerInv, Object target){
        super(type,windowId);
        this.playerInv = newSlotList(playerInv);
        this.plr=playerInv.player;
        T tgt = null;
        try {
            if (target instanceof BlockPos t) {
                tgt = (T) Minecraft.getInstance().level.getBlockEntity(t);
            } else tgt = null;
        } catch (ClassCastException ex){
            ex.printStackTrace();
        }
        this.target = tgt;
        getData().listenChanged(this::onDataChange);
    }


    protected <T extends IFastItemHandler & IItemHandlerModifiable> InventoryWrapper<T> newSlotList(T handler){
        InventoryWrapper<T> list = new InventoryWrapper<>(handler);
        list.index=inventories.size();
        inventories.add(list);
        return list;
    }
    protected InventoryWrapper<FastItemStackHandler> newSlotList(Inventory playerInv){
        return newSlotList(new FastItemStackHandler(playerInv.items));
    }


    public SyncableCompound getData() {
        return target.getData();
    }

    public void onDataChange(){
        if(plr instanceof ServerPlayer sp) {
            getData().syncRemote(new SyncTarget.Window(new DataAccessor.Remote(sp)));
        }
        else{
            getData().syncRemote(new SyncTarget.Window(DataAccessor.LOCAL));
        }
    }

    public void handleSlotPacket(SPacketSlotInteraction packet, Player player) {
        if (packet instanceof SPacketSlotClicked sc) onSlotClicked(sc);
        else if(packet instanceof SPacketSlotKeyPressed skp) onSlotKeyPressed(skp);
    }
    public void onSlotKeyPressed(SPacketSlotKeyPressed packet){
        //TODO impl drop.
    }
    public void onSlotClicked(SPacketSlotClicked packet) {
        InventoryWrapper<?> inventory = inventories.get(packet.getInventory());
        int slot = packet.getSlot();
        ItemStack stack = inventory.getItem(slot);
        //Shift held: switch item between inventories.
        if(packet.shiftHeld()){
            switchInventories(inventory,slot);
        }
        //Not holding anything.
        else if(interaction.held.isEmpty()){
            //Take entire item from slot
            if(packet.isLeftClick()){
                interaction.held = stack;
                inventory.setItem(slot,ItemStack.EMPTY);
            }
            //Right click: take half
            else{
                ItemStack half = stack.copy();
                half.setCount((int)Math.ceil(half.getCount()/2.0));
                stack.shrink(half.getCount());
                interaction.held=half;
            }
        }
        //Holding something
        else{
            //Left click swap item.
            if(packet.isLeftClick()){
                if(!interaction.isHolding(stack) || stack.getCount() == stack.getMaxStackSize()){
                    if(inventory.mayPlace(slot,interaction.held)) {
                        ItemStack pre = interaction.held;
                        interaction.held = stack;
                        inventory.setItem(slot, pre);
                    }
                }
                else {
                    ItemStack pre = interaction.held;
                    int toAdd = Math.min(stack.getMaxStackSize()-stack.getCount(),pre.getCount());
                    pre.shrink(toAdd);
                    stack.grow(toAdd);
                }
            }
            //Right click put one.
            else {
                stack.grow(1);
                interaction.held.shrink(1);
            }
        }
    }

    public void switchInventories(InventoryWrapper list, int slot){
        ItemStack stack = list.getItem(slot);
        ItemStack toTransfer = stack.copy();
        for (InventoryWrapper<?> inventory : inventories) {
            if(toTransfer.isEmpty()){
                break;
            }
            if(inventory.equals(list)) continue;
            toTransfer = inventory.insert(toTransfer);
        }
        list.getItem(slot).shrink(stack.getCount()-toTransfer.getCount());
    }

    public UserInteractionState getInteractionState() {
        return interaction;
    }

    /**
     * Shift left/right: switch inventory
     *
     * Held Empty:
     * 	-Left: take whole stack.
     * 	-Right: split stack, transfer stack gets more or equal.
     *
     * Held Not Empty:
     * 	-Same type
     * 		-Right: Place maximum
     * 		-Left: Place one
     * 	-Different type:
     * 		-Right/left: Swap stacks
     * Quickbinds
     * 	-Double click stack: maximize stack. (shift clicking conflicts)
     * 	-Drop Key - drop, hold ctrl: drop all.
     * 	-Click stack outside GUI: drop
     *
     */
}
