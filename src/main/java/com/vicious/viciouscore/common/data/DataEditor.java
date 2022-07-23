package com.vicious.viciouscore.common.data;

import com.vicious.viciouscore.ViciousCore;
import net.minecraft.server.level.ServerPlayer;

public class DataEditor {
    public static DataEditor LOCAL = new Local();

    public static DataEditor.Remote of(ServerPlayer sender) {
        return new DataEditor.Remote(sender);
    }

    public boolean isRemoteEditor(){
        return this instanceof Remote;
    }
    public void securityViolated(String s){}

    /**
     * For when the server modifies local data.
     */
    public static class Local extends DataEditor{}

    /**
     * In client-server comms, on the server end all player packet requests go through a RemoteDataEditor.
     * This is used to check whether editing data is permitted.
     */
    public static class Remote extends DataEditor{
        public ServerPlayer editor;
        public Remote(ServerPlayer editor){
            this.editor=editor;
        }
        @Override
        public void securityViolated(String violationInfo){
            ViciousCore.logger.fatal(editor.getName() + " has committed a security violation! " + violationInfo);
        }
    }
}
