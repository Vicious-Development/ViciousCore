package com.vicious.viciouscore.common.data;


public class SyncTarget {
    public final DataAccessor editor;

    public SyncTarget(DataAccessor editor) {
        this.editor = editor;
    }
    public static class Window extends SyncTarget{
        public final int window;

        public Window(DataAccessor editor, int window) {
            super(editor);
            this.window = window;
        }
    }
}
