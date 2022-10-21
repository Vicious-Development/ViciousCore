package com.vicious.viciouscore.client.gui.widgets;

public enum ControlFlag {
    RESPONDTOHOVER,
    RESPONDTOCLICK,
    RESPONDTODRAG,
    //This is used when determining which widget is currently being hovered over. If a widget lacks this response type then it and all its children will be ignored.
    //Stopping responses from raytrace will cause all above responses to be stopped as well.
    RESPONDTORAYTRACE,
    //When a widget is covered or not covered by other widgets.
    RESPONDTOCOVER,
    RESPONDTOEXPOSED,
    //When a child widget updates its extents
    RESPONDTOCHILDUPDATES,
    SHOULDBROADCASTUPDATES,

    //Determines whether the widget should render
    VISIBLE,
    //These are added and remove per tick. If the CLICKED flag is in the control set, the widget has been clicked.
    CLICKED,
    HOVERED
}
