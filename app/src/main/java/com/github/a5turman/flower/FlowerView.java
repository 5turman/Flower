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
 * Created by 5turman on 6/15/2017.
 */
public class FlowerView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

    private float[] leafCoords;

    public FlowerView(Context context) {
        this(context, null);
    }

    public FlowerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(ContextCompat.getColor(context, R.color.flower));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = canvas.getWidth() / 2;
        int cy = canvas.getHeight() / 2;

        if (path.isEmpty()) {
            rebuildPath(cx, cy);
        }

        int numberOfLeafs = 9;

        float degrees = 360f / numberOfLeafs;

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        for (int i = 0; i < numberOfLeafs; ++i) {
            canvas.drawPath(path, paint);
            canvas.rotate(degrees, cx, cy);
        }

        canvas.restore();
    }

    private void rebuildPath(int x0, int y0) {
        float r = Math.min(x0, y0);
        PointF point1 = MathUtils.calculate(x0, y0, r, leafCoords[0], leafCoords[1]);
        PointF point2 = MathUtils.calculate(x0, y0, r, leafCoords[2], leafCoords[3]);

        path.moveTo(x0, y0);
        path.cubicTo(point1.x, point1.y, point2.x, point2.y, x0 + r, y0);
        path.cubicTo(point2.x, y0 + (y0 - point2.y), point1.x, y0 + (y0 - point1.y), x0, y0);
    }

    public void setLeafCoords(float[] coords) {
        leafCoords = coords;
        path.reset();
        invalidate();
    }

}
