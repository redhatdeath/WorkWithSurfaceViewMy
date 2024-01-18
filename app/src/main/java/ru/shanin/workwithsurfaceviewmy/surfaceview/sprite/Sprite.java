package ru.shanin.workwithsurfaceviewmy.surfaceview.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite {
    public static long stepCount = 0;
    private static final int FRAME_SIZE_PIXELS = 128;
    private static final int FRAMES_COUNT_ROW = 4;
    private static final int FRAMES_COUNT_COL = 32;

    private final Bitmap bitmap;
    private final Rect[][] frames;
    private final int speed;

    /**
     * speed :
     * 1 - fast
     * 2 - normal
     * 3 - slow
     */

    public Sprite(Bitmap bitmap, int speed_in) {
        this.bitmap = bitmap;
        speed = speed_in;
        frames = new Rect[FRAMES_COUNT_ROW][FRAMES_COUNT_COL];
        switch (speed) {
            case 1:
            case 2:
                for (int i = 0; i < FRAMES_COUNT_ROW; i++)
                    for (int j = 0; j < FRAMES_COUNT_COL; j++)
                        frames[i][j] = new Rect(
                                FRAME_SIZE_PIXELS * j, FRAME_SIZE_PIXELS * i,
                                FRAME_SIZE_PIXELS * (j + 1), FRAME_SIZE_PIXELS * (i + 1)
                        );
                break;
            case 3:
                for (int i = 0; i < FRAMES_COUNT_ROW; i++)
                    for (int j = 0; j < FRAMES_COUNT_COL / 2; j++) {
                        frames[i][j] = new Rect(
                                FRAME_SIZE_PIXELS * j, FRAME_SIZE_PIXELS * i,
                                FRAME_SIZE_PIXELS * (j + 1), FRAME_SIZE_PIXELS * (i + 1)
                        );
                        frames[i][FRAMES_COUNT_COL / 2 + j] = frames[i][j];
                    }
                break;
        }
    }

    public void draw(Canvas canvas, Rect rect, int ward) {
        int currentFrame = (int) (stepCount / speed) % FRAMES_COUNT_COL;
        canvas.drawBitmap(bitmap, frames[ward][currentFrame], rect, new Paint());
    }

    public static void nextFrame() {
        stepCount++;
    }
}