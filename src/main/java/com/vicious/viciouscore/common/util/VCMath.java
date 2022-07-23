package com.vicious.viciouscore.common.util;

import net.minecraft.world.phys.Vec3;

public class VCMath {
    public static double getDistance(Vec3 v1, Vec3 v2){
        return Math.sqrt(Math.pow(v2.x-v1.x,2)+Math.pow(v2.y-v1.y,2)+Math.pow(v2.z-v1.z,2));
    }
}
