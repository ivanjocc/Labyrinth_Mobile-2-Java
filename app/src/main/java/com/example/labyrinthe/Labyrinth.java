package com.example.labyrinthe;

import android.content.Context;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Random;

import java.util.Random;

import android.graphics.Canvas;

public class Labyrinth {
    private ArrayList<Wall> walls;
    private ArrayList<Hole> holes;
    private int width;
    private int height;
    private Random random = new Random();

    public Labyrinth(int width, int height) {
        this.width = width;
        this.height = height;
        walls = new ArrayList<>();
        holes = new ArrayList<>();
        initializeLabyrinth();
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

    private void initializeLabyrinth() {
        createWalls(10);
        createHoles(10);
    }

    private void createWalls(int count) {
        for (int i = 0; i < count; i++) {
            int wallWidth = 100 + random.nextInt(150);
            int wallHeight = 50 + random.nextInt(150);
            int x = random.nextInt(width - wallWidth);
            int y = random.nextInt(height - wallHeight);
            if (!checkOverlap(x, y, Math.max(wallWidth, wallHeight) / 2)) {
                walls.add(new Wall(x, y, wallWidth, wallHeight));
            }
        }
    }

    private void createHoles(int count) {
        for (int i = 0; i < count; i++) {
            int radius = 40 + random.nextInt(30);
            int x, y;
            boolean collision;
            do {
                collision = false;
                x = random.nextInt(width - radius * 2) + radius;
                y = random.nextInt(height - radius * 2) + radius;
                if (checkOverlap(x, y, radius)) {
                    collision = true;
                }
            } while (collision);
            holes.add(new Hole(x, y, radius));
        }
    }

    public boolean checkOverlap(int x, int y, int radius) {
        for (Wall wall : walls) {
            if (x + radius > wall.getX() && x - radius < wall.getX() + wall.getWidth() &&
                    y + radius > wall.getY() && y - radius < wall.getY() + wall.getHeight()) {
                return true;
            }
        }
        for (Hole hole : holes) {
            double distance = Math.sqrt(Math.pow(x - hole.getX(), 2) + Math.pow(y - hole.getY(), 2));
            if (distance < radius + hole.getRadius()) {
                return true;
            }
        }
        return false;
    }

    public Ball createBall(int ballRadius) {
        int x, y;
        boolean valid;
        do {
            valid = true;
            x = random.nextInt(width - ballRadius * 2) + ballRadius;
            y = random.nextInt(height - ballRadius * 2) + ballRadius;
            if (checkOverlap(x, y, ballRadius)) {
                valid = false;
            }
        } while (!valid);
        return new Ball(x, y, ballRadius);
    }

    public boolean isCollision(float newX, float newY, int radius) {
        for (Wall wall : walls) {
            if (newX + radius > wall.getX() && newX - radius < wall.getX() + wall.getWidth() &&
                    newY + radius > wall.getY() && newY - radius < wall.getY() + wall.getHeight()) {
                return true;
            }
        }
        return false;
    }
}
