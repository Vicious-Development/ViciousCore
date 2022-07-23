package com.vicious.viciouscore.client.util;

public class Extents {
    public final Vector2i TOPLEFT;
    public final Vector2i BOTTOMRIGHT;
    public Extents(Vector2i topleft, Vector2i bottomright){
        this.TOPLEFT=topleft;
        this.BOTTOMRIGHT=bottomright;
    }
    public boolean isWithin(Vector2i point){
        return isWithin(point.x,point.y);
    }
    public boolean isWithin(int x, int y){
        return isWithinX(x) && isWithinY(y);
    }
    public boolean isWithinX(int x){
        return x >= TOPLEFT.x && x <= BOTTOMRIGHT.x;
    }
    public boolean isWithinY(int y){
        return y >= TOPLEFT.y && y <= BOTTOMRIGHT.y;
    }

}
