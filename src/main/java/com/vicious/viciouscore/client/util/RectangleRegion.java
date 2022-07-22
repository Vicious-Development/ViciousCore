package com.vicious.viciouscore.client.util;

public class RectangleRegion {
    //lefttop
    public double x;
    public double y;
    //rightbottom
    public double x1;
    public double y1;

    public RectangleRegion(double x, double y, double x1, double y1){
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }
    public RectangleRegion(double centerx, double centery, double squareLength){
        this.x = centerx - squareLength/2;
        this.x1 = centerx + squareLength/2;
        this.y = centery - squareLength/2;
        this.y1 = centery + squareLength/2;
    }
    public static RectangleRegion createTopLeftCornerRectangle(double x, double y, double vertSize, double horizSize){
        return new RectangleRegion(
                x,
                y,
                x + horizSize,
                y + vertSize
        );
    }
    public static RectangleRegion createCenterRectangle(double centerx, double centery, double vertSize, double horizSize){
        return new RectangleRegion(centerx - horizSize/2,
            centery + vertSize/2,
            centerx - horizSize/2,
            centery + vertSize/2);
    }
    //Checks if in rectangle, edge inclusive.
    public boolean contains(double x, double y){
        return x >= this.x && x <= x1 && y >= this.y && y <= y1;
    }
    public void translate(Vector2i translation){
        x += translation.x;
        y += translation.y;
        x1 += translation.x;
        y1 += translation.y;
    }
}
