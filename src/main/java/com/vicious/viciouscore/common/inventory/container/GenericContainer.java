package com.vicious.viciouscore.common.inventory.container;

import com.mojang.blaze3d.platform.InputConstants;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.implementations.SyncableInventory;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.inventory.FastItemStackHandler;
import com.vicious.viciouscore.common.inventory.slots.SlotPlayerInv36;
import com.vicious.viciouscore.common.inventory.slots.VCSlot;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotClicked;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotInteraction;
import com.vicious.viciouscore.common.network.packets.slot.SPacketSlotKeyPressed;
import com.vicious.viciouscore.common.util.RangedInteger;
import com.vicious.viciouscore.common.util.item.ItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GenericContainer<T extends ISyncableCompoundHolder> extends AbstractContainerMenu implements ISyncableCompoundHolder{
    protected boolean isClient = false;
    public static final RangedInteger PLAYER_MAIN_INVENTORY = new RangedInteger(0, 36);
    protected ServerPlayer listener;
    protected List<InventoryWrapper> inventories;
    protected UserInteractionState interaction;
    public final T target;

    /**
     * Server End
     */
    public GenericContainer(@Nullable MenuType<?> type, int windowId, Inventory playerInv, T target) {
        super(type, windowId);
        newSlotList(playerInv);
        this.target = target;
        getData().listenChanged(this::onDataChange);
    }

    /**
     * Client End
     */
    public GenericContainer(@Nullable MenuType<?> type, int windowId, Inventory playerInv, Object target){
        super(type,windowId);
        newSlotList(playerInv);
        isClient=true;
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

    public boolean transferStackOutOfContainer(Player player, ItemStack stack){
        return mergeInto(PLAYER_MAIN_INVENTORY, stack, false);
    }
    protected boolean mergeInto(RangedInteger destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return moveItemStackTo(sourceItemStack, destinationZone.firstIndex, destinationZone.firstIndex+destinationZone.size, fillFromEnd);
    }
    protected void addHoloInventory(Inventory invPlayer, int hotbarx, int hotbary, int slotxspacing, int slotyspacing){
        InventoryWrapper list = newSlotList();
        for (int x = 0; x < 9; x++) {
            list.add(new SlotPlayerInv36(invPlayer, x, hotbarx + slotxspacing * x, hotbary, PLAYER_MAIN_INVENTORY));
        }
        // Add the rest of the players inventory to the gui
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int slotNumber = 9 + y * 9 + x;
                int xpos = hotbarx + x * slotxspacing;
                int ypos = hotbary - 2 - (y+1) * slotyspacing;
                list.add(new SlotPlayerInv36(invPlayer, slotNumber,  xpos, ypos, PLAYER_MAIN_INVENTORY));
            }
        }
    }

    protected InventoryWrapper newSlotList(FastItemStackHandler handler){
        InventoryWrapper list = new InventoryWrapper(handler);
        list.index=slots.size();
        inventories.add(list);
        return list;
    }
    protected InventoryWrapper newSlotList(Inventory playerInv){
        return newSlotList(new FastItemStackHandler(playerInv.items));
    }

    @Override
    public void addSlotListener(ContainerListener list) {
        super.addSlotListener(list);
        if(list instanceof ServerPlayer plr){
            listener = plr;
        }
    }

    @Override
    public void removeSlotListener(ContainerListener list) {
        super.removeSlotListener(list);
        if(list instanceof ServerPlayer){
            listener = null;
        }
    }

    public SyncableCompound getData() {
        return target.getData();
    }

    @Override
    public void removed(Player plr) {
        super.removed(plr);
        if(plr instanceof ServerPlayer) listener = null;
    }

    public void onDataChange(){
        if(isClient){
            getData().syncRemote(new SyncTarget.Window(DataAccessor.LOCAL,containerId));
        }
        else{
            getData().syncRemote(new SyncTarget.Window(new DataAccessor.Remote(listener),containerId));
        }
    }

    public void handleSlotPacket(SPacketSlotInteraction packet, ServerPlayer player) {
        if (packet instanceof SPacketSlotClicked sc) onSlotClicked(sc);
        else if(packet instanceof SPacketSlotKeyPressed skp) onSlotKeyPressed(skp);
    }
    public void onSlotKeyPressed(SPacketSlotKeyPressed packet){
        //TODO impl drop.
    }
    public void onSlotClicked(SPacketSlotClicked packet) {
        InventoryWrapper inventory = inventories.get(packet.getInventory());
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
            if(packet.isLeftClick() || !interaction.isHolding(stack) || stack.getCount() == stack.getMaxStackSize()){
                if(inventory.mayPlace(slot,interaction.held)) {
                    ItemStack pre = interaction.held;
                    interaction.held = stack;
                    inventory.setItem(slot, pre);
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
        for (InventoryWrapper inventory : inventories) {
            if(toTransfer.isEmpty()){
                break;
            }
            if(inventory.equals(list)) continue;
            toTransfer = inventory.insert(toTransfer);
        }
        list.getItem(slot).shrink(stack.getCount()-toTransfer.getCount());
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
