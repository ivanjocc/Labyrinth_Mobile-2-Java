package com.example.labyrinthe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

public class GameView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float ax = 0, ay = 0;
    private int screenWidth, screenHeight;
    private Random random = new Random();

    private Labyrinth labyrinth;
    private Ball ball;

//    public GameView(Context context) {
//        super(context);
//        initGameComponents();
//        setupAccelerometer(context);
//    }

    public GameView(Context context) {
        super(context);
        setupScreenDimensions();
        initGameComponents();
        setupAccelerometer(context);
    }

    private void setupScreenDimensions() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void initGameComponents() {
        labyrinth = new Labyrinth(screenWidth, screenHeight);
        // Asegurar que la bola no se inicializa dentro de un muro o un hoyo
        int x, y;
        boolean valid;
        do {
            valid = true;
            x = random.nextInt(screenWidth - 40) + 20; // Asumiendo radio de 20
            y = random.nextInt(screenHeight - 40) + 20;
            for (Wall wall : labyrinth.getWalls()) {
                if (x > wall.getX() && x < wall.getX() + wall.getWidth() &&
                        y > wall.getY() && y < wall.getY() + wall.getHeight()) {
                    valid = false;
                    break;
                }
            }
            for (Hole hole : labyrinth.getHoles()) {
                double distance = Math.sqrt(Math.pow(x - hole.getX(), 2) + Math.pow(y - hole.getY(), 2));
                if (distance < hole.getRadius() + 20) { // 20 es el radio de la bola
                    valid = false;
                    break;
                }
            }
        } while (!valid);
        ball = new Ball(x, y, 20);
    }


    private void setupAccelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        labyrinth.draw(canvas);
        ball.draw(canvas);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ax = event.values[0];
        ay = event.values[1];

        updateBallPosition();
        invalidate();
    }

    private void updateBallPosition() {
        float dx = ax * -0.5f;
        float dy = ay * 0.5f;
        ball.updatePosition(dx, dy);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        nothing here
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void resume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        sensorManager.unregisterListener(this);
    }
}
