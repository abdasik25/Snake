package com.example.snake;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * The type Banana.
 */
public class Banana extends ImageView {

    private float dx, dy;

    /**
     * Instantiates a new Banana.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public Banana(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        final View v = this;

        final Context context1 = context;

        setImageDrawable(getResources().getDrawable(R.drawable.banana, null));

        if (isInEditMode())
            return;

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((SnakeBehavior) context1).addToFruitList(v);
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(100, 100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        System.out.println("On touch event");
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = x - getX();
                dy = y - getY();
                break;
            case MotionEvent.ACTION_MOVE:
                setX(x - dx);
                setY(y - dy);
                break;
        }

        return true;
    }
}
