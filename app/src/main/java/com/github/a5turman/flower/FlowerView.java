package com.github.a5turman.flower;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by 5turman on 6/19/2017.
 */
public class FlowerView extends ViewGroup {

    /**
     * The initial fling velocity is divided by this amount.
     */
    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private final FlowerDrawable flower;
    private final View view;

    Scroller mScroller;
    ValueAnimator mScrollAnimator;
    GestureDetector detector;

    public FlowerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int color = ContextCompat.getColor(context, R.color.flower);
        flower = new FlowerDrawable(color);

        view = new View(context);
        view.setBackground(flower);

        addView(view);

        mScroller = new Scroller(context, null, true);

        mScrollAnimator = ValueAnimator.ofFloat(0, 1);
        mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tickScrollAnimation();
            }
        });

        detector = new GestureDetector(context, gestureListener);
        detector.setIsLongpressEnabled(false);

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = detector.onTouchEvent(event);
                if (!result && event.getAction() == MotionEvent.ACTION_UP) {
                    stopScrolling();
                    result = true;
                }
                return result;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int min = Math.min(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)
        );

        view.measure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(min, min);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        view.layout(0, 0, r - l, b - t);
    }

    public void setLeafCoords(float[] coords) {
        flower.setLeafCoords(coords);
    }

    protected void rotateFlower(int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        view.setRotation(rotation);
    }

    private void tickScrollAnimation() {
        if (mScroller.computeScrollOffset()) { // not finished
            rotateFlower(mScroller.getCurrY());
        } else {
            mScrollAnimator.cancel();
            onScrollFinished();
        }
    }

    /**
     * Force a stop to all flower motion. Called when the user taps during a fling.
     */
    private void stopScrolling() {
        mScroller.forceFinished(true);
        onScrollFinished();
    }

    /**
     * Called when the user finishes a scroll action.
     */
    private void onScrollFinished() {
        decelerate();
    }

    private boolean isAnimationRunning() {
        return !mScroller.isFinished();
    }

    /**
     * Enable hardware acceleration (consumes memory)
     */
    void accelerate() {
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    /**
     * Disable hardware acceleration (releases memory)
     */
    void decelerate() {
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float cx = view.getX() + view.getWidth() / 2;
            float cy = view.getY() + view.getHeight() / 2;

            float scrollTheta = MathUtils.vectorToScalarScroll(
                    distanceX,
                    distanceY,
                    e2.getX() - cx,
                    e2.getY() - cy);

            float rotation = view.getRotation() - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE;

            rotateFlower((int) rotation);

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // The user is interacting with the flower, so we want to turn on acceleration
            // so that the interaction is smooth.
            accelerate();
            if (isAnimationRunning()) {
                stopScrolling();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float cx = view.getX() + view.getWidth() / 2;
            float cy = view.getY() + view.getHeight() / 2;

            // Set up the Scroller for a fling
            float scrollTheta = MathUtils.vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - cx,
                    e2.getY() - cy);

            mScroller.fling(
                    0,
                    (int) view.getRotation(),
                    0,
                    (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                    0,
                    0,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE);

            // Start the animator and tell it to animate for the expected duration of the fling.
            mScrollAnimator.setDuration(mScroller.getDuration());
            mScrollAnimator.start();

            return true;
        }

    };

}
