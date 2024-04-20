package com.example.labyrinthe;

import android.content.Context;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Random;

public class Labyrinth {
    private ArrayList<Wall> walls;
    private ArrayList<Hole> holes;

    private int width;
    private int height;

    public Labyrinth(int width, int height) {
        this.width = width;
        this.height = height;
        walls = new ArrayList<>();
        holes = new ArrayList<>();
        initializeLabyrinth();
    }

    private void initializeLabyrinth() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(width - 100);
            int y = random.nextInt(height - 100);
            int wallWidth = 50 + random.nextInt(150);
            int wallHeight = 50 + random.nextInt(150);
            walls.add(new Wall(x, y, wallWidth, wallHeight));
        }

        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(width - 50);
            int y = random.nextInt(height - 50);
            int radius = 20 + random.nextInt(30);
            holes.add(new Hole(x, y, radius));
        }
    }

    public void draw(Canvas canvas) {
        for (Wall wall : walls) {
            wall.draw(canvas);
        }
        for (Hole hole : holes) {
            hole.draw(canvas);
        }
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }
}
