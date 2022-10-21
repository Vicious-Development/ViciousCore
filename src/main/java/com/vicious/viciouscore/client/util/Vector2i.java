package com.vicious.viciouscore.client.util;

import java.util.Objects;

public class Vector2i {
    public final int x;
    public final int y;
    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i other) {
        x=other.x;
        y= other.y;
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
    public Vector2i multiply(Vector2f scale) {
        return new Vector2i((int) (x*scale.x), (int) (y*scale.y));
    }

    public Vector2i maximum(Vector2i lt) {
        return new Vector2i(Math.max(lt.x, x), Math.max(lt.y, y));
    }
    public Vector2i minimum(Vector2i lt) {
        return new Vector2i(Math.min(lt.x, x), Math.min(lt.y, y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2i = (Vector2i) o;
        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2i withX(int x) {
        return new Vector2i(x,y);
    }
    public Vector2i withY(int y) {
        return new Vector2i(x,y);
    }
}
