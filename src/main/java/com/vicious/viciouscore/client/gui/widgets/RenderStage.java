package com.vicious.viciouscore.client.gui.widgets;

public enum RenderStage {
    //Executed before the children AND the parent are rendered
    PRE,
    //Executed only when the parent renders
    SELFPRE,
    //Executed after the parent renders
    SELFPOST,
    //Executed after the children AND the parent have rendered.
    POST
}
