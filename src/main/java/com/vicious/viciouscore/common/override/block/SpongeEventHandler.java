package com.vicious.viciouscore.common.override.block;

import com.vicious.viciouscore.common.util.reflect.Reflection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * While Sponge is installed, some forge events are never posted. This system catches them.
 */
public class SpongeEventHandler {
    Field causeField = Reflection.getField(Cause.class, "cause");
    public static List<Consumer<IBlockState>> blockPlaceHandlers = new ArrayList<>();
    @Listener
    /**
     * Called when a block changes in the world.
     * If the cause was NOT a player, gets the pos using the cause STACK field.
     * Else gets it via the final block transaction.
     */
    public void handleBlockEvent(ChangeBlockEvent.Post ev){
        //System.out.println("CTX: " + ev.getContext());
       // System.out.println("CAUSE: " + ev.getCause());
       // System.out.println("TRANSACTION: " + ev.getTransactions().get(0).getFinal());
        List<Transaction<BlockSnapshot>> trans = ev.getTransactions();
        for (Transaction<BlockSnapshot> tran : trans) {
            BlockSnapshot bk = tran.getFinal();
            Optional<Location<World>> loc = bk.getLocation();
            if(loc.isPresent()){
                Location<World> l = loc.get();
                Optional<Integer> wid = l.getExtent().getProperties().getAdditionalProperties().getInt(DataQuery.of("SpongeData", "dimensionId"));
                if(wid.isPresent()){
                    BlockOverrideHandler.fixPos(DimensionManager.getWorld(wid.get()), new BlockPos(l.getX(),l.getY(),l.getZ()));
                }
            }
        }
        //SpongeModEventManager
        /*if(((Object[])Reflection.accessField(ev.getCause(),causeField)).length > 0){
            Optional<SpongeBlockSnapshot> snapshot = ev.getCause().first(SpongeBlockSnapshot.class);
        }
        for (Transaction<BlockSnapshot> transaction : ev.getTransactions()) {
            BlockSnapshot bk = transaction.getFinal();

            if (ev.getContext().containsKey(EventContextKeys.PLAYER_PLACE))
                //WorldManager.getWorldByDimensionId(ev.getContext());
            if (bk.getLocation().isPresent()) {
                for (Consumer<IBlockState> blockPlaceHandler : blockPlaceHandlers) {
                    //blockPlaceHandler.accept();
                }
            }
        }*/
    }
}
