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

    // Método para dibujar el laberinto en el canvas
    public void draw(Canvas canvas) {
        for (Wall wall : walls) {
            wall.draw(canvas);
        }
        for (Hole hole : holes) {
            hole.draw(canvas);
        }
    }

    // Método para obtener la lista de muros
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    // Método para obtener la lista de hoyos
    public ArrayList<Hole> getHoles() {
        return holes;
    }

    private void initializeLabyrinth() {
        // Asignar número y dimensiones de los muros y hoyos aleatoriamente
        createWalls(5);
        createHoles(3);
    }

    private void createWalls(int count) {
        for (int i = 0; i < count; i++) {
            int wallWidth = 50 + random.nextInt(150);
            int wallHeight = 50 + random.nextInt(150);
            int x = random.nextInt(width - wallWidth);
            int y = random.nextInt(height - wallHeight);
            walls.add(new Wall(x, y, wallWidth, wallHeight));
        }
    }

    private void createHoles(int count) {
        for (int i = 0; i < count; i++) {
            int radius = 20 + random.nextInt(30);
            int x, y;
            boolean collision;
            do {
                collision = false;
                x = random.nextInt(width - radius * 2) + radius;
                y = random.nextInt(height - radius * 2) + radius;
                // Verificar colisiones con muros
                for (Wall wall : walls) {
                    if (x + radius > wall.getX() && x - radius < wall.getX() + wall.getWidth() &&
                            y + radius > wall.getY() && y - radius < wall.getY() + wall.getHeight()) {
                        collision = true;
                        break;
                    }
                }
            } while (collision); // Reintentar si hay colisión
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
