package com.vicious.viciouscore.common.data;


public class SyncTarget {
    public final DataEditor editor;

    public SyncTarget(DataEditor editor) {
        this.editor = editor;
    }
    public static class Window extends SyncTarget{
        public final int window;

        public Window(DataEditor editor, int window) {
            super(editor);
            this.window = window;
        }
    }
}
