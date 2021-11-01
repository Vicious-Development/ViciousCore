package com.vicious.viciouscore.common.util.tracking;

import com.vicious.viciouscore.common.util.tracking.interfaces.TickableTrackableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//Used to track variables that should be updated on the DB
public class TrackingHandler {
    //OnWorldTick
    private Stack<JSONTrackable<?>> dirtyJsons = new Stack<>();
    private List<TickableTrackableValue> tickables = new ArrayList<>();
    public void update() {
        try {
            tick();
            int count = 0;
            //TODO: Make count a CFG option
            while (count < 5) {
                count++;
                if (dirtyJsons.size() <= 0) break;
                dirtyJsons.peek().overWriteFile();
                dirtyJsons.pop();
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public void addTickable(TickableTrackableValue v){
        tickables.add(v);
    }
    public void tick(){
        for (TickableTrackableValue tickable : tickables) {
            tickable.tick();
        }
    }

    public <T extends JSONTrackable<T>> void queueJSONUpdate(JSONTrackable<T> jsonTrackable) {
        dirtyJsons.add(jsonTrackable);
    }
}
