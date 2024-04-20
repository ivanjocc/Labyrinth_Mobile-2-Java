package com.example.labyrinthe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

public class GameView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float ax = 0, ay = 0;
    private int screenWidth, screenHeight;
    private Random random = new Random();

    private Labyrinth labyrinth;
    private Ball ball;

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
        ball = labyrinth.createBall(20);
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
        float dx = ax * -0.5f; // Invertir dirección x si es necesario
        float dy = ay * 0.5f;
        float newX = ball.getX() + dx;
        float newY = ball.getY() + dy;

        newX = Math.max(ball.getRadius(), Math.min(newX, screenWidth - ball.getRadius()));
        newY = Math.max(ball.getRadius(), Math.min(newY, screenHeight - ball.getRadius()));

        if (!labyrinth.isCollision(newX, newY, ball.getRadius())) {
            ball.setX(newX);
            ball.setY(newY);
            checkForHole();
        } else {
            handleCollision();
        }
    }

    private void handleCollision() {
        Toast.makeText(getContext(), "Collision with wall!", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing here
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

    private void checkForHole() {
        for (Hole hole : labyrinth.getHoles()) {
            double distance = Math.sqrt(Math.pow(ball.getX() - hole.getX(), 2) + Math.pow(ball.getY() - hole.getY(), 2));
            if (distance < ball.getRadius() + hole.getRadius()) {
                gameOver();
                return;
            }
        }
    }

    private void gameOver() {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(getContext(), "You lost! Falling into a hole!", Toast.LENGTH_SHORT).show();
            ((Activity) getContext()).finish();
        });
    }
}
