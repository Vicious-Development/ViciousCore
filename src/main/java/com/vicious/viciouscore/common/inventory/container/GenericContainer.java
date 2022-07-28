package com.vicious.viciouscore.common.inventory.container;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.SyncTarget;
import com.vicious.viciouscore.common.data.holder.ISyncableCompoundHolder;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import com.vicious.viciouscore.common.inventory.slots.SlotPlayerInv36;
import com.vicious.viciouscore.common.inventory.slots.VCSlot;
import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericContainer<T extends ISyncableCompoundHolder> extends AbstractContainerMenu implements ISyncableCompoundHolder{
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    // slot index is the unique index for all slots in this container i.e. 0 - 35 for invPlayer. This may be subject to change in the future.
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    protected static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected boolean isClient = false;
    public static final RangedInteger PLAYER_MAIN_INVENTORY = new RangedInteger(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOT_COUNT);
    public static final RangedInteger PLAYER_HOTBAR = new RangedInteger(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT);
    protected ServerPlayer listener;


    public final T target;
    /**
     * Does not include the player slots.
     */
    public final ArrayList<VCSlot> machineSlots = new ArrayList<>();
    public final ArrayList<VCSlot> playerSlots = new ArrayList<>();

    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 125;

    public GenericContainer(@Nullable MenuType<?> type, int windowId, T target) {
        super(type, windowId);
        this.target = target;
        getData().listenChanged(this::onDataChange);
    }

    /**
     * Client End
     * @param type
     * @param windowId
     * @param inventory - an inventory (idk what inventory though and I don't use it anyways).
     * @param target - the target of this container.
     */
    public GenericContainer(@Nullable MenuType<?> type, int windowId, Inventory inventory, Object target){
        super(type,windowId);
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

    public VCSlot overwriteSlot(VCSlot slotOld, VCSlot slotNew){
        if(slotOld instanceof SlotPlayerInv36){
            playerSlots.set(slotOld.index,slotNew);
        }
        else machineSlots.set(slotOld.index,slotNew);
        int index = slots.indexOf(slotOld);
        if(index != -1) slots.set(index, slotNew);
        return slotNew;
    }

    public boolean transferStackOutOfContainer(Player player, ItemStack stack){
        boolean successfulTransfer = mergeInto(PLAYER_HOTBAR, stack, true);
        if (!successfulTransfer) {
            successfulTransfer = mergeInto(PLAYER_MAIN_INVENTORY, stack, true);
        }
        return successfulTransfer;
    }
    protected boolean mergeInto(RangedInteger destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return moveItemStackTo(sourceItemStack, destinationZone.firstIndex, destinationZone.firstIndex+destinationZone.size, fillFromEnd);
    }
    protected void addHoloInventory(Inventory invPlayer, int hotbarx, int hotbary, int slotxspacing, int slotyspacing){
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new SlotPlayerInv36(invPlayer, x, hotbarx + slotxspacing * x, hotbary, PLAYER_HOTBAR));
        }
        // Add the rest of the players inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = hotbarx + x * slotxspacing;
                int ypos = hotbary - 2 - (y+1) * slotyspacing;
                addSlot(new SlotPlayerInv36(invPlayer, slotNumber,  xpos, ypos, PLAYER_MAIN_INVENTORY));
            }
        }
    }
    protected <T extends VCSlot> T addSlot(T slotIn) {
        if(slotIn instanceof SlotPlayerInv36)
            playerSlots.add(slotIn);
        else
            machineSlots.add(slotIn);
        return (T) super.addSlot(slotIn);
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
        if(list instanceof ServerPlayer plr){
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
}
