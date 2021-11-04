package com.vicious.viciouscore.common.util.tracking;

import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VCTrackingHandler extends TrackingHandler {
    public static VCTrackingHandler instance;

    public VCTrackingHandler() {
        instance = this;
    }

    public static void init() {
        new VCTrackingHandler();
        Trackable.setHandler(instance);
        TrackableValue.globalHandler = instance;
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            try {
                instance.update();
            } catch (Exception e) {
                System.out.println("Failed to update trackable: " + e.getMessage());
                e.printStackTrace();
            }
            //TODO CONFIGURABLE
        }, 0, 50, TimeUnit.MILLISECONDS);
    }
}
