package com.vicious.viciouscore.client.util;

import java.util.List;
import java.util.Objects;

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
    public int getWidth(){
        return BOTTOMRIGHT.x-TOPLEFT.x;
    }
    public int getHeight(){
        return BOTTOMRIGHT.y-TOPLEFT.y;
    }

    public static Extents combined(Extents... extents){
        Vector2i lt = new Vector2i(Integer.MAX_VALUE,Integer.MAX_VALUE);
        Vector2i rb = new Vector2i(Integer.MIN_VALUE,Integer.MIN_VALUE);
        for (Extents extent : extents) {
            if(extent != null) {
                lt = extent.TOPLEFT.minimum(lt);
                rb = extent.BOTTOMRIGHT.maximum(rb);
            }
        }
        return new Extents(lt,rb);
    }
    public static Extents combined(List<Extents> exts){
        if(exts.size() == 0) return null;
        return combined(exts.toArray(new Extents[0]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extents extents = (Extents) o;
        return Objects.equals(TOPLEFT, extents.TOPLEFT) && Objects.equals(BOTTOMRIGHT, extents.BOTTOMRIGHT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TOPLEFT, BOTTOMRIGHT);
    }
}
