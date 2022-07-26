package com.vicious.viciouscore.common.tile.tickless;

import com.vicious.viciouscore.common.tile.PhysicalTE;
import com.vicious.viciouscore.common.tile.VCBlockEntities;
import com.vicious.viciouscore.common.util.SidedExecutor;
import com.vicious.viciouslib.util.interfaces.INotifiable;
import com.vicious.viciouslib.util.interfaces.INotifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//TODO: make multiblockcomponents notify each other less.
public class TileMultiBlockComponent extends PhysicalTE implements INotifier<Object> {
    public List<INotifiable<Object>> parents = new ArrayList<>();
    public long lastTick = -1;
    private boolean hasBeenValidatedAlready = false;

    public TileMultiBlockComponent(BlockPos pos, BlockState blockState) {
        super(VCBlockEntities.MULTIBLOCKCOMPONENT.get(), pos, blockState);
        //Prevents infinite looping on chunk gen.
    }

    /**
     * Equivalent of validate()
     */
    @Override
    public void setLevel(Level p_155231_) {
        super.setLevel(p_155231_);
        notifyNeighbors();
        notifyParent();
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public void notifyNeighbors(){
        SidedExecutor.serverOnly(()->{
            long worldTick = level != null ? level.getGameTime() : -1;
            forNeighborBlockTiles((bs,te,bpos)->{
                Block b = bs.getBlock();
                TileMultiBlockComponent tmbc = null;
                if(te instanceof TileMultiBlockComponent){
                    tmbc = (TileMultiBlockComponent) te;
                }
                if (tmbc != null) {
                    if(tmbc.lastTick != worldTick) {
                        tmbc.lastTick = worldTick;
                        List<INotifiable<Object>> parentList = tmbc.getParents();
                        for (INotifiable<Object> parent : parentList) {
                            if (!parents.contains(parent)) parents.add(parent);
                        }
                        tmbc.setParents(parents);
                    }
                }
            });
        });
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        notifyParent();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        notifyParent();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        notifyNeighbors();
        notifyParent();
    }



    /**
     * Notify the parent that it should do something.
     * Remove the parent if the parent is no longer a valid parent.
     */
    public void notifyParent(){
        SidedExecutor.serverOnly(()->{
            for (int i = 0; i < parents.size(); i++) {
                INotifiable<Object> parent = parents.get(i);
                if(parent instanceof BlockEntity) {
                    if (((BlockEntity) parent).isRemoved()) {
                        parents.remove(i);
                        i--;
                    }
                    else if (parent != null) {
                        parent.notify(this);
                    }
                }
                else {
                    parents.remove(i);
                    i--;
                }
            }
        });
    }

    @Override
    public void addParent(INotifiable<Object> parent) {
        if(!parents.contains(parent)) parents.add(parent);
    }

    @Override
    public List<INotifiable<Object>> getParents() {
        return parents;
    }

    @Override
    public void setParents(List<INotifiable<Object>> parents) {
        this.parents = parents;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        notifyParent();
        return super.getUpdateTag();
    }
}
