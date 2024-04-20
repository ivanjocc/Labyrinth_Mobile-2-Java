package com.example.labyrinthe;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

public class Hole {
    private int x;
    private int y;
    private int radius;
    private Paint paint;

    public Hole(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
    }

   public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public boolean contains(float px, float py) {
        return Math.sqrt(Math.pow(px - x, 2) + Math.pow(py - y, 2)) <= radius;
    }
}
