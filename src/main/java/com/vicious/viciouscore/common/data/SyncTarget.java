package com.vicious.viciouscore.common.data;


public class SyncTarget {
    public final DataAccessor editor;

    public SyncTarget(DataAccessor editor) {
        this.editor = editor;
    }

    public static class Window extends SyncTarget{
        public Window(DataAccessor editor) {
            super(editor);
        }
    }
}
