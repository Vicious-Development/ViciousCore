package com.vicious.viciouscore.common.inventory.container;

import com.vicious.viciouscore.common.data.CompoundSyncableData;
import com.vicious.viciouscore.common.inventory.slots.SlotPlayerInv36;
import com.vicious.viciouscore.common.inventory.slots.VCSlot;
import com.vicious.viciouscore.common.util.RangedInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class GenericContainer<T> extends AbstractContainerMenu {
    protected final CompoundSyncableData syncableData = new CompoundSyncableData();
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    // slot index is the unique index for all slots in this container i.e. 0 - 35 for invPlayer. This may be subject to change in the future.
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    protected static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected final ArrayList<RangedInteger> slotGroups = new ArrayList<>();
    public static final RangedInteger PLAYER_MAIN_INVENTORY = new RangedInteger(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOT_COUNT);
    public static final RangedInteger PLAYER_HOTBAR = new RangedInteger(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT);
    protected final T obj;
    public final ArrayList<VCSlot> slots = new ArrayList<>();
    public final ArrayList<VCSlot> playerSlots = new ArrayList<>();

    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 125;

    protected GenericContainer(@Nullable MenuType<?> type, int windowId, T obj) {
        super(type, windowId);
        this.obj =obj;
    }

    public abstract VCSlot overwriteSlot(VCSlot slotOld, VCSlot slotNew);

    public abstract void handleStateChange(CompoundTag nbt);
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
            int slotNumber = x;
            addSlot(new SlotPlayerInv36(invPlayer, slotNumber, hotbarx + slotxspacing * x, hotbary, PLAYER_HOTBAR));
        }
        // Add the rest of the players inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = hotbarx + x * slotxspacing;
                int ypos = hotbary - 2 - (y+1) * slotyspacing;
                addSlot(new SlotPlayerInv36(invPlayer, slotNumber,  xpos, ypos, PLAYER_HOTBAR));
            }
        }
    }
    protected <T extends VCSlot> T addSlot(T slotIn) {
        if(slotIn instanceof SlotPlayerInv36)
            playerSlots.add(slotIn);
        else
            slots.add(slotIn);
        return (T) super.addSlot(slotIn);
    }

    public CompoundSyncableData getCompoundData() {
        return syncableData;
    }
}
