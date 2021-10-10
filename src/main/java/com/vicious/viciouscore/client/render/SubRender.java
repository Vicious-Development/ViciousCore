package com.vicious.viciouscore.client.render;

//Unexpirable render
public class SubRender extends ViciousRender {

    public SubRender() {
        super(0);
    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
