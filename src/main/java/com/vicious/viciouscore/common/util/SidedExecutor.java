package com.vicious.viciouscore.common.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class SidedExecutor {
    public static void clientOnly(Runnable run){
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT,()->()->{
            run.run();
            return null;
        });
    }

    public static void serverOnly(Runnable run) {
        DistExecutor.unsafeCallWhenOn(Dist.DEDICATED_SERVER,()->()->{
            run.run();
            return null;
        });
    }
}
