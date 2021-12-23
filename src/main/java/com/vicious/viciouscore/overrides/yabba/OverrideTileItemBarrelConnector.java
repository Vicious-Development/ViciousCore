package com.vicious.viciouscore.overrides.yabba;

import com.latmod.yabba.tile.BarrelNetwork;
import com.latmod.yabba.tile.ItemBarrel;
import com.latmod.yabba.tile.TileItemBarrelConnector;
import com.vicious.viciouscore.common.util.reflect.IFieldCloner;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public class OverrideTileItemBarrelConnector extends TileItemBarrelConnector implements IFieldCloner {
    public Map<Item,ArrayList<ItemBarrel>> barrels = new HashMap<>();
    public boolean remapEmpty = false;
    public OverrideTileItemBarrelConnector(Object og){
        clone(og);
    }
    public OverrideTileItemBarrelConnector(){
    }
    private final Comparator<ItemBarrel> BARREL_COMPARATOR = (o1, o2) -> {
        int i = Boolean.compare(o1.type.isEmpty(), o2.type.isEmpty());
        i = i == 0 ? Boolean.compare(o2.barrel.isLocked(), o1.barrel.isLocked()) : i;
        return i == 0 ? Double.compare(pos.distanceSq(((TileEntity) o1.barrel.block).getPos()), pos.distanceSq(((TileEntity) o2.barrel.block).getPos())) : i;
    };

    @Override
    public void invalidate()
    {
        if (hasWorld())
        {
            BarrelNetwork.get(getWorld()).refresh();
        }

        super.invalidate();
    }

    @Override
    public void setWorld(World world)
    {
        super.setWorld(world);

        if (hasWorld())
        {
            BarrelNetwork.get(getWorld()).refresh();
        }
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean notifyBlock()
    {
        return false;
    }

    private void addToList(HashSet<ItemBarrel> scanned, BlockPos pos, EnumFacing from)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, from))
        {
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, from);

            if (itemHandler instanceof ItemBarrel && scanned.add((ItemBarrel) itemHandler))
            {
                for (EnumFacing facing1 : EnumFacing.VALUES)
                {
                    if (facing1 != from)
                    {
                        addToList(scanned, pos.offset(facing1), facing1.getOpposite());
                    }
                }
            }
        }
    }
    public void mapBarrels(){
        barrels.clear();
        HashSet<ItemBarrel> scanned = new HashSet<>();
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            addToList(scanned, pos.offset(facing), facing.getOpposite());
        }

        linkedBarrels = new ArrayList<>(scanned);
        for (ItemBarrel itemBarrel : scanned) {
            mapBarrel(itemBarrel);
        }
        linkedBarrels.sort(BARREL_COMPARATOR);
    }
    public void mapBarrel(ItemBarrel barrel){
        Item type = barrel.type.getItem();
        ArrayList<ItemBarrel> list = barrels.get(type);
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(barrel);
        if(!barrels.containsKey(type)){
            barrels.put(type,list);
        }
    }
    public void remapEmptyBarrels(){
        ArrayList<ItemBarrel> empties = barrels.get(Items.AIR);
        if(empties == null) return;
        for (int i = 0; i < empties.size(); i++) {
            if(!empties.get(i).isEmpty()){
                ItemBarrel barrel = empties.remove(i);
                i--;
                mapBarrel(barrel);
            }
        }
    }
    public void remapNotEmptyBarrels(Item prev){
        ArrayList<ItemBarrel> nempties = barrels.get(prev);
        for (int i = 0; i < nempties.size(); i++) {
            if(nempties.get(i).isEmpty()){
                ItemBarrel barrel = nempties.remove(i);
                i--;
                mapBarrel(barrel);
            }
        }
    }
    public void mapIfNeeded(){
        if(linkedBarrels == null || linkedBarrels.isEmpty()) mapBarrels();
    }

    @Nullable
    private ItemBarrel getAt(int slot)
    {
        if(world == null || world.isRemote) return null;
        if (linkedBarrels == null) mapBarrels();

        if (slot <= 0 || slot > linkedBarrels.size())
        {
            return null;
        }

        ItemBarrel barrel = linkedBarrels.get(slot - 1);
        return barrel == null || barrel.barrel.block.isBarrelInvalid() ? null : barrel;
    }

    @Override
    public int getSlots()
    {
        mapIfNeeded();
        return 1 + linkedBarrels.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot == 0)
        {
            return ItemStack.EMPTY;
        }

        ItemBarrel barrel = getAt(slot);
        return barrel == null ? ItemStack.EMPTY : barrel.getStackInSlot(1);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        if (slot == 0)
        {
            return false;
        }

        mapIfNeeded();
        ArrayList<ItemBarrel> lst = barrels.get(stack.getItem());
        ArrayList<ItemBarrel> lst2 = barrels.get(Items.AIR);
        if(lst == null) lst = lst2;
        else if(lst2 != null) lst.addAll(lst2);
        for (ItemBarrel barrel : lst)
        {
            if (barrel.isItemValid(0, stack))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    //Ignore slot, find item in HashMap.
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if (stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else if (slot != 0)
        {
            return stack;
        }
        mapIfNeeded();
        ArrayList<ItemBarrel> lst = barrels.get(stack.getItem());
        if(lst != null) {
            for (ItemBarrel barrel : lst) {
                if (!barrel.isEmpty()) {
                    stack = barrel.insertItem(0, stack, simulate);

                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        lst = barrels.get(Items.AIR);
        if(lst != null) {
            for (ItemBarrel barrel : lst) {
                if (barrel.isEmpty()) {
                    stack = barrel.insertItem(0, stack, simulate);
                    if (!barrel.isEmpty()) remapEmpty = true;
                    if (stack.isEmpty()) {
                        remapEmptyBarrels();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if(remapEmpty) remapEmptyBarrels();
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot == 0)
        {
            return ItemStack.EMPTY;
        }

        ItemBarrel barrel = getAt(slot);
        ItemStack ret = barrel == null ? ItemStack.EMPTY : barrel.extractItem(1, amount, simulate);
        if(barrel != null && barrel.isEmpty() && !ret.isEmpty()) remapNotEmptyBarrels(ret.getItem());
        return ret;
    }

    @Override
    public void updateBarrels()
    {
        super.updateBarrels();
        barrels.clear();
    }

    @Override
    public String getName() {
        return "overriddenbarreltile";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
