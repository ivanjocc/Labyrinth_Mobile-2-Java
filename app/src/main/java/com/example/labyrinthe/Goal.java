package com.example.labyrinthe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Goal {
    private int x, y, radius;

    public Goal(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.GREEN);
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
}
