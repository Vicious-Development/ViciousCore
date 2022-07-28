package com.vicious.viciouscore.common.data;


import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class SyncTarget {
    public final DataAccessor editor;

    public SyncTarget(DataAccessor editor) {
        this.editor = editor;
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public static class Window extends SyncTarget{
        public final int window;
        public Window(DataAccessor editor, int window) {
            super(editor);
            this.window = window;
        }

        @Override
        public void toBytes(FriendlyByteBuf buf) {
            buf.writeInt(window);
        }
    }
    public static class Tile extends SyncTarget{
        public final BlockPos position;

        public Tile(DataAccessor editor, BlockPos position) {
            super(editor);
            this.position = position;
        }

        @Override
        public void toBytes(FriendlyByteBuf buf) {
            buf.writeBlockPos(position);
        }
    }
}
