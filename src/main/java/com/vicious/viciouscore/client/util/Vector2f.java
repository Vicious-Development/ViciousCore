package com.vicious.viciouscore.client.util;

public class Vector2f {
    public final float x;
    public final float y;
    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f offsetVector) {
        return new Vector2f(x+offsetVector.x,y+ offsetVector.y);
    }
    public Vector2f add(float x, float y) {
        return new Vector2f(this.x+x,this.y+y);
    }
    public static Vector2f of(float x, float y){
        return new Vector2f(x,y);
    }
    public Vector2f multiply(Vector2f scale) {
        return new Vector2f(x*scale.x,y*scale.y);
    }
}
