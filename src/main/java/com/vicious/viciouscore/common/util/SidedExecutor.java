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
}
