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

    /**
     * Helper method for translating (x,y) scroll vectors into scalar rotation of the flower.
     *
     * @param dx The x component of the current scroll vector.
     * @param dy The y component of the current scroll vector.
     * @param x  The x position of the current touch, relative to the flower center.
     * @param y  The y position of the current touch, relative to the flower center.
     * @return The scalar representing the change in angular position for this scroll.
     */
    static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        // get the length of the vector
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        // decide if the scalar should be negative or positive by finding
        // the dot product of the vector perpendicular to (x,y).
        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }

}
