package com.example.labyrinthe;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float ax = 0, ay = 0;

    private Labyrinth labyrinth;
    private Ball ball;

    public GameView(Context context) {
        super(context);
        initGameComponents();
        setupAccelerometer(context);
    }

    private void initGameComponents() {
        labyrinth = new Labyrinth(500, 800);
        ball = new Ball(250, 400, 20);
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
