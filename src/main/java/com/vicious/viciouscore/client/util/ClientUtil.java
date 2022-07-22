package com.vicious.viciouscore.client.util;

import com.drathonix.zeroenergy.CapabilityHandler;
import com.drathonix.zeroenergy.common.tileentity.TEGeneric;
import com.drathonix.zeroenergy.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;

public class ClientUtil {
    public static final CapabilityHandler CAPABILITYHANDLER = new CapabilityHandler();

    public static <T extends TEGeneric> T tileFromPacket(@Nonnull PacketBuffer buf, Class<T> tileType){
        if (buf == null) {
            throw new IllegalArgumentException("Null packet buffer");
        }
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            BlockPos pos = buf.readBlockPos();
            T tile = Util.getTileEntityOfExpectedType(Minecraft.getInstance().world, pos, tileType);
            if (tile == null) {
                throw new IllegalStateException("Failed to locate a tile in the client level, this really do be an F moment. EXPECTED POSITION: " + pos + ", TILE: " + tileType);
            }
            return tile;
        });
    }
}
