package ru.shanin.workwithsurfaceviewmy.surfaceview;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import ru.shanin.workwithsurfaceviewmy.surfaceview.timers.TimerCounterFramesIncrements;
import ru.shanin.workwithsurfaceviewmy.surfaceview.timers.TimerDrawCanvasOnDisplay;
import ru.shanin.workwithsurfaceviewmy.surfaceview.timers.TimerWorkWithCoordinate;

public class ViewDraw extends SurfaceView implements SurfaceHolder.Callback {
    private final TimerCounterFramesIncrements timerCounter;
    private final TimerDrawCanvasOnDisplay timerDrawOnCanvas;
    private final TimerWorkWithCoordinate timerWorkWithCoordinate;

    public ViewDraw(Context context) {
        super(context);
        getHolder().addCallback(this);
        timerDrawOnCanvas = new TimerDrawCanvasOnDisplay(getContext(), getHolder());
        timerCounter = new TimerCounterFramesIncrements();
        timerWorkWithCoordinate = new TimerWorkWithCoordinate(context);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        timerCounter.startWork();
        timerDrawOnCanvas.startWork();
        timerWorkWithCoordinate.startWork();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                retry = timerCounter.stopWork() || timerDrawOnCanvas.stopWork() || timerWorkWithCoordinate.stopWork();
            } catch (Exception ignored) {
                retry = false;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            //TODO method to work with coordinate
            timerDrawOnCanvas.updateTouchCoordinate((int) event.getX(), (int) event.getY());
            timerWorkWithCoordinate.updateTouchCoordinate((int) event.getX(), (int) event.getY());
            //Log.w("onTouchEvent", "Click on coord : " + (int) event.getX() + ", " + (int) event.getY());
        }
        return true;
    }

}
