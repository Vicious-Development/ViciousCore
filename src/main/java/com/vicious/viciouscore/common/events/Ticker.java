package com.vicious.viciouscore.common.events;

import com.vicious.viciouscore.common.util.server.ServerHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Ticker {
    public static final Set<VCTask> tasks = new HashSet<>();
    public static final List<VCTask> toRemove = new LinkedList<>();
    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event){
        if(event.phase == TickEvent.Phase.END){
            for (VCTask task : tasks) {
                if(task.shouldRun(ServerHelper.server.getNextTickTime()-1)){
                    task.run();
                }
                if(task.isCancelled()) toRemove.add(task);
            }
            for (VCTask vcTask : toRemove) {
                tasks.remove(vcTask);
            }
        }
    }
}
