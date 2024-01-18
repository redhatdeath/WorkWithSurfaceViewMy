package ru.shanin.workwithsurfaceviewmy.surfaceview.timers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Timer;
import java.util.TimerTask;

import ru.shanin.workwithsurfaceviewmy.surfaceview.sprite.Field;

public class TimerDrawCanvasOnDisplay {
    private final Timer timer;
    private final TimerTaskDrawCanvas task;
    private boolean running;


    public TimerDrawCanvasOnDisplay(Context context, SurfaceHolder surfaceHolder) {
        timer = new Timer();
        task = new TimerTaskDrawCanvas(surfaceHolder, context);
    }


    public void startWork() {
        // ms, for 30 tic/sec
        int PERIOD = 16;//16.32.64
        // ms
        int DELAYED_START = 0;
        running = true;
        timer.scheduleAtFixedRate(task, DELAYED_START, PERIOD);
    }

    public boolean stopWork() {
        running = false;
        timer.cancel();
        return running;
    }

    public void updateTouchCoordinate(int newX, int newY) {
        task.updateTouchCoordinate(newX, newY);
    }

    private class TimerTaskDrawCanvas extends TimerTask {
        private final SurfaceHolder surfaceHolder;

        private int step;
        private int startX;
        private int stopX;
        private int start;
        private int stop;
        private int startY;
        private int stopY;
        private int touchX;
        private int touchY;

        public TimerTaskDrawCanvas(SurfaceHolder surfaceHolder, Context context) {
            showLog("TimerTaskCounterIncrements");
            this.surfaceHolder = surfaceHolder;
            initDisplayMetrics(context);
            Field.initField(context);
        }

        private void initDisplayMetrics(Context context) {
            showLog("initDisplayMetrics");
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            showLog("ScreenSize : x =  " + dm.widthPixels + ", y = " + dm.heightPixels);
            int minPx = Math.min(dm.widthPixels, dm.heightPixels);
            step = minPx > 1000 ? 128 : 80;
            showLog("step = " + step);
            int delta = (minPx - (step << 3) >> 1);
            showLog("delta = " + delta);
            start = minPx * 0 + delta;
            stop = minPx * 1 - delta;
            startX = startY = start + (step >> 1);
            stopX = stopY = stop - (step >> 1);
        }

        public void updateTouchCoordinate(int newX, int newY) {
            touchX = newX;
            touchY = newY;
        }

        @Override
        public void run() {
            long t0 = System.nanoTime();
            if (running) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        drawLines(canvas);
                        Field.drawFieldLines(canvas);
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            long t1 = System.nanoTime();
            long dt = (t1 - t0) / 1000000 + 1;
            if (dt > 29) showLog("dt = " + dt);
            if (dt > 32) showLogErr("Too long draw canvas !!!");
        }

        private void drawLines(Canvas canvas) {
            Paint p = new Paint();
            p.setColor(Color.RED);
            canvas.drawRect(
                    new Rect(start + 0, start + 0, stop - 0, stop - 0), p);
            p.setColor(Color.BLACK);
            canvas.drawRect(
                    new Rect(start + 8, start + 8, stop - 8, stop - 8), p);
            p.setColor(Color.RED);
            for (int i = 0; i < 7; i++) {
                //horizontal
                canvas.drawRect(new Rect(
                        startX + 0, startY + (step >> 1) + i * step - 2,
                        stopX - 0, startY + (step >> 1) + i * step + 2
                ), p);
                // vertical
                canvas.drawRect(new Rect(
                        startX + (step >> 1) + i * step - 2, startY + 0,
                        startX + (step >> 1) + i * step + 2, stopY - 0
                ), p);
            }
            p.setColor(Color.BLUE);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(touchX, touchY, 24, p);
            p.setColor(Color.GREEN);
            for (int i = 0; i < TimerWorkWithCoordinate.coordinate[0].length; i++) {
                for (int j = 0; j < TimerWorkWithCoordinate.coordinate[1].length; j++) {
                    canvas.drawCircle(
                            TimerWorkWithCoordinate.coordinate[0][i],
                            TimerWorkWithCoordinate.coordinate[0][j],
                            15, p);
                }
            }
        }

        private void showLog(String message) {
            String LOG_TAG = TimerTaskDrawCanvas.class.getSimpleName() + "Log";
            Log.d(LOG_TAG, LOG_TAG + ": " + message);
        }

        private void showLogErr(String message) {
            String LOG_TAG = TimerTaskDrawCanvas.class.getSimpleName() + "Log";
            Log.e(LOG_TAG, LOG_TAG + ": " + message);
        }
    }

}
