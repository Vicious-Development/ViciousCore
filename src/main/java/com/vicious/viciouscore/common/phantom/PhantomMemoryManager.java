package com.vicious.viciouscore.common.phantom;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class PhantomMemoryManager {
    public static PhantomMemoryManager instance;
    public static PhantomMemoryManager getInstance(){
        if(instance == null) instance = new PhantomMemoryManager();
        return instance;
    }
    private int nextID = -1;
    private final Map<Object,PhantomMemory> memories = new HashMap<>();
    private final Set<PhantomMemoryTickable> tickableMemories = new HashSet<>();
    @SuppressWarnings("unchecked")
    public <T extends PhantomMemory> T setup(Function<Object[],T> constructor, Object... associations){
        PhantomMemory target = null;
        for (Object association : associations) {
            PhantomMemory mem = memories.get(association);
            if(mem != null){
                if(target == null){
                    target=mem;
                }
                else{
                    if(target != mem){
                        throw new PhantomDisassociationException(mem + " is associated with " + association + " but is not equal to " + target + "!");
                    }
                }
            }
        }
        if(target == null) target = add(constructor.apply(associations));
        return (T) target;
    }
    public <T extends PhantomMemory> T add(T memory){
        memory.ID = nextID();
        for (Object association : memory.keySet) {
            memories.put(association,memory);
        }
        if(memory instanceof PhantomMemoryTickable tickable) tickableMemories.add(tickable);
        return memory;
    }
    public void remove(PhantomMemory memory){
        for (Object association : memory.keySet) {
            memories.remove(association);
        }
        if(memory instanceof PhantomMemoryTickable) tickableMemories.remove(memory);
    }
    public void tick(){
        for (PhantomMemoryTickable mem : tickableMemories) {
            if(mem.shouldTick()) mem.tick();
        }
    }
    public int nextID(){
        nextID++;
        return nextID;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if(event.phase == TickEvent.Phase.END) {
            tick();
        }
    }
}
