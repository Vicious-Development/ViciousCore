package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.ViciousCore;
import com.vicious.viciouscore.common.network.VCNetwork;
import com.vicious.viciouscore.common.network.VCPacket;
import net.minecraft.server.level.ServerPlayer;

public class DataEditor {
    public static DataEditor LOCAL = new Local();
    public void sendPacket(VCPacket packet){}

    public static DataEditor.Remote of(ServerPlayer sender) {
        return new DataEditor.Remote(sender);
    }

    public boolean isRemoteEditor(){
        return this instanceof Remote;
    }
    public void securityViolated(String s){}

    /**
     * For when the server or client modifies local data.
     */
    public static class Local extends DataEditor{}

    /**
     * For when the client sends updates to the server.
     */
    public static class Server extends DataEditor{
        @Override
        public void sendPacket(VCPacket packet) {
            VCNetwork.getInstance().sendToServer(packet);
        }
    }

    /**
     * Used on the server side to control client editing permission. A remote editor is only used when a client to server data packet is received.
     */
    public static class Remote extends DataEditor{
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
