package com.vicious.viciouscore.common.capability.types.keypresshandler;

import com.vicious.viciouscore.common.capability.interfaces.IVCCapabilityHandler;
import com.vicious.viciouscore.common.keybinding.CommonKeyBinding;
import com.vicious.viciouscore.common.keybinding.CommonKeyBindings;

import java.util.Map;

public class KeyPressHandler implements IVCCapabilityHandler {
    private Map<Integer, CommonKeyBinding> bindings;
    public KeyPressHandler(){
        bindings = CommonKeyBindings.copyBindings();
    }
    public boolean isDown(CommonKeyBinding commonkey) {
        return bindings.get(commonkey.ID).isDown;
    }
    public boolean isDown(int commonKeyId) {
        return bindings.get(commonKeyId).isDown;
    }
    public Map<Integer,CommonKeyBinding> getBindings(){
        return bindings;
    }
    public void setDown(int code, boolean pressed) {
        bindings.get(code).isDown=pressed;
    }
}
