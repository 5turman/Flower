package com.github.a5turman.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 5turman on 6/16/2017.
 */
public class LeafView extends View {

    interface DumpCallback {
        void onDump(float[] coords);
    }

    public static final int PEAK_RADIUS_DP = 8;
    public static final int POINT_RADIUS_DP = 4;

    private final Paint leafPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float[] coords;

    private final int peakRadius;
    private final int radius;

    private final Path path = new Path();
    private PointF point1;
    private PointF point2;

    private DumpCallback dumpCallback;

    public LeafView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float density = context.getResources().getDisplayMetrics().density;
        peakRadius = (int) (density * PEAK_RADIUS_DP);
        radius = (int) (density * POINT_RADIUS_DP);

        leafPaint.setColor(ContextCompat.getColor(context, R.color.flower));
        pointPaint.setColor(ContextCompat.getColor(context, R.color.accent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path.isEmpty()) {
            rebuildPath(canvas.getWidth(), canvas.getHeight());
        }

        canvas.drawCircle(point1.x, point1.y, radius, pointPaint);
        canvas.drawCircle(point2.x, point2.y, radius, pointPaint);

        canvas.drawPath(path, leafPaint);
    }

    public void setCoords(float[] coords) {
        this.coords = coords;
    }

    public void updatePoint(int index, float x, float y) {
        if (index < 0 || index > 2) return;

        ((index == 0) ? point1 : point2).set(x, y);
        path.reset();
        invalidate();
    }

    public int peak(float x, float y) {
        if (intersects(point1, x, y)) {
            return 0;
        }

        return intersects(point2, x, y) ? 1 : -1;
    }

    public void dump(DumpCallback callback) {
        dumpCallback = callback;
        path.reset();
        invalidate();
    }

    private void rebuildPath(int maxWidth, int maxHeight) {
        path.reset();

        int dx = maxWidth / 6;

        int x0 = dx;
        int y0 = maxHeight / 2;

        // the distance between (x0, y0) and (5 * dx, y0)
        float r = 4 * dx;

        if (point1 == null) {
            if (coords != null) {
                point1 = MathUtils.calculate(x0, y0, r, coords[0], coords[1]);
                point2 = MathUtils.calculate(x0, y0, r, coords[2], coords[3]);
            } else {
                int y = y0 / 2;
                point1 = new PointF(3 * dx, y);
                point2 = new PointF(4 * dx, y);
            }
        }

        path.moveTo(x0, y0);
        path.cubicTo(point1.x, point1.y, point2.x, point2.y, x0 + r, y0);
        path.cubicTo(point2.x, y0 + (y0 - point2.y), point1.x, y0 + (y0 - point1.y), x0, y0);

        if (dumpCallback != null) {
            float d1 = MathUtils.distance(point1.x, point1.y, x0, y0);
            float d2 = MathUtils.distance(point2.x, point2.y, x0, y0);

            float alpha1 = (float) Math.asin((y0 - point1.y) / d1);
            float alpha2 = (float) Math.asin((y0 - point2.y) / d2);

            float r1 = d1 / r;
            float r2 = d2 / r;

            float[] coords = {
                    r1, alpha1,
                    r2, alpha2
            };

            dumpCallback.onDump(coords);
            dumpCallback = null;
        }
    }

    private boolean intersects(PointF point, float x, float y) {
        float dx = point.x - x;
        float dy = point.y - y;
        return (dx * dx + dy * dy <= peakRadius * peakRadius);
    }

}
