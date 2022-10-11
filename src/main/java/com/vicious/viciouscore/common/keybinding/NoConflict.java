package com.vicious.viciouscore.common.keybinding;

import net.minecraftforge.client.settings.IKeyConflictContext;

public class NoConflict implements IKeyConflictContext {
    public static final IKeyConflictContext NOCONFLICT = new NoConflict();

    private NoConflict(){}

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
        return false;
    }
}
