package com.github.a5turman.flower;

import android.graphics.PointF;

/**
 * Created by 5turman on 6/16/2017.
 */
public class MathUtils {

    static float distance(float x1, float y1, float x2, float y2) {
        float a = x2 - x1;
        float b = y2 - y1;
        return (float) Math.sqrt(a * a + b * b);
    }

    static PointF calculate(float cx, float cy, float radius, float r, float angle) {
        float dx = (float) (r * Math.cos(angle));
        float dy = (float) (r * Math.sin(angle));

        return new PointF(
                cx + radius * dx,
                cy - radius * dy
        );
    }

}
