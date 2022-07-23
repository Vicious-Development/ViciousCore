package com.vicious.viciouscore.client.util;

public class Vector2i {
    public final int x;
    public final int y;
    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2i add(Vector2i offsetVector) {
        return new Vector2i(x+offsetVector.x,y+ offsetVector.y);
    }
    public Vector2i add(int x, int y) {
        return new Vector2i(this.x+x,this.y+y);
    }
    public static Vector2i of(int x, int y){
        return new Vector2i(x,y);
    }
    public Vector2i multiply(Vector2i scale) {
        return new Vector2i(x*scale.x,y*scale.y);
    }
}
