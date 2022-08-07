package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.server.level.ServerPlayer;

public class DataAccessor {
    public static DataAccessor REMOTE = new Remote(null);
    public static DataAccessor LOCAL = new Local();
    public static DataAccessor WORLD = new Local();
    /**
     * Acts as a local accessor, intended only for permitting authorized users access to data.
     */
    public static DataAccessor FORCEREMOTE = new Local();
    public void sendPacket(VCPacket packet){}

    public static DataAccessor.Remote of(ServerPlayer sender) {
        return new DataAccessor.Remote(sender);
    }

    public boolean isRemoteEditor(){
        return this instanceof Remote;
    }
    public void securityViolated(String s){}

    /**
     * For when the server or client modifies local data.
     */
    public static class Local extends DataAccessor {}

    /**
     * For when the client sends updates to the server.
     */
    public static class Server extends DataAccessor {
        @Override
        public void sendPacket(VCPacket packet) {
            VCNetwork.getInstance().sendToServer(packet);
        }
    }

    /**
     * Used on the server side to control client editing/reading permission.
     */
    public static class Remote extends DataAccessor {
        public ServerPlayer editor;
        public Remote(ServerPlayer editor){
            this.editor=editor;
        }

        @Override
        public void sendPacket(VCPacket packet) {
            VCNetwork.getInstance().sendToPlayer(editor,packet);
        }

        @Override
        public void securityViolated(String violationInfo){
            ViciousCore.logger.fatal(editor.getName() + " has committed a security violation! " + violationInfo);
        }
    }
}
