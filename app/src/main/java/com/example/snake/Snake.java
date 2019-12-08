package com.example.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * The type Snake.
 */
public class Snake extends View {

    private int speed = 10;
    private Paint paint;
    private int startBodyCount = 1;

    private int currentColor = Color.BLACK;
    private static int STEPS_COUNT = 7;


    private PointF startPosition;
    private int radius = 20;

    private float desiredX, desiredY;
    private boolean isMoving;

    private SnakeBehavior gameManager;

    private float dx, dy;

    private ArrayList<SnakeBodyCell> bodyCells;

    /**
     * Instantiates a new Snake.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public Snake(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(currentColor);

        startPosition = new PointF(500, 500);

        bodyCells = new ArrayList<>();


        for (int i = 0; i < startBodyCount; i++) {
            bodyCells.add(new SnakeBodyCell(startPosition.x, startPosition.y));
            startPosition.offset(0, -radius * 2);
        }

        if (isInEditMode())
            return;

        gameManager = ((SnakeBehavior) context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Rect globalRect = new Rect();
        getGlobalVisibleRect(globalRect);
        dx = globalRect.left - getLeft();
        dy = globalRect.top - getTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSnakeBody(canvas);

        if (isMoving && !trashCollision()) {
            moveTo(desiredX, desiredY);
            checkAppleCollision();
        }

    }

    private void drawSnakeBody(Canvas canvas) {
        for (SnakeBodyCell bodyCell : bodyCells) {
            canvas.drawCircle(bodyCell.getPos().x, bodyCell.getPos().y, radius, paint);
        }
    }

    /**
     * Move to.
     *
     * @param x the x
     * @param y the y
     */
    public void moveTo(float x, float y) {
        float dx = x - bodyCells.get(0).getPos().x;
        float dy = y - bodyCells.get(0).getPos().y;

        if (Math.abs(dx) > speed)
            dx = Math.signum(dx) * speed;
        if (Math.abs(dy) > speed)
            dy = Math.signum(dy) * speed;

        bodyCells.get(0).addToPos(dx, dy);

        for (int i = 1; i < bodyCells.size(); i++) {
            bodyCells.get(i).setPos(bodyCells.get(i - 1).getPrevPos());
        }

        invalidate();
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                desiredX = x;
                desiredY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                desiredX = x;
                desiredY = y;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }

        if (isMoving) {
            invalidate();
            requestLayout();
            return true;
        }
        return false;


    }

    private boolean trashCollision() {
        List<View> trashList = gameManager.getTrash();
        Rect trashRect = new Rect();

        PointF head = bodyCells.get(0).getPos();
        Rect thisRect = new Rect((int) (head.x - radius + dx), (int) (head.y - radius + dy), (int) (head.x + radius + dx), (int) (head.y + radius + dy));

        for (View trash : trashList) {
            trash.getGlobalVisibleRect(trashRect);
            if (trashRect.intersect(thisRect) && trashRect.intersect(bodyCells.get(0).getFutureRect())) {
                return true;
            }
        }

        return false;
    }

    private void checkAppleCollision() {
        List<View> appleList = gameManager.getFruits();
        View apple;

        PointF head = bodyCells.get(0).getPos();

        Rect appleRect = new Rect();
        Rect thisRect = new Rect((int) (head.x - radius + dx), (int) (head.y - radius + dy), (int) (head.x + radius + dx), (int) (head.y + radius + dy));

        Iterator it = appleList.iterator();
        while (it.hasNext()) {
            apple = ((View) it.next());
            apple.getGlobalVisibleRect(appleRect);
            if (appleRect.intersect(thisRect)) {
                it.remove();
                gameManager.CollectFruit(apple);
                STEPS_COUNT--;
                paint.setColor(ColorUtils.getColor((float) (1. / STEPS_COUNT)));
                PointF tail = bodyCells.get(bodyCells.size() - 1).getPos();
                bodyCells.add(new SnakeBodyCell(tail.x, tail.y - radius * 2));
                return;
            }
        }
    }

    private class SnakeBodyCell {

        private PointF prevPos;
        private PointF pos;

        /**
         * Instantiates a new Snake body cell.
         *
         * @param x the x
         * @param y the y
         */
        public SnakeBodyCell(float x, float y) {
            prevPos = new PointF(x, y);
            pos = new PointF(x, y);
        }

        /**
         * Add to pos.
         *
         * @param dx the dx
         * @param dy the dy
         */
        public void addToPos(float dx, float dy) {
            prevPos.x = pos.x;
            prevPos.y = pos.y;

            pos.offset(dx, dy);
        }

        /**
         * Gets future rect.
         *
         * @return the future rect
         */
        public Rect getFutureRect() {
            PointF direction = new PointF();
            direction.x = desiredX - pos.x;
            direction.y = desiredY - pos.y;

            float magnitude = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
            direction.x /= magnitude;
            direction.y /= magnitude;

            PointF futurePos = new PointF();
            futurePos.x = pos.x + direction.x * speed * 2;
            futurePos.y = pos.y + direction.y * speed * 2;

            return new Rect((int) (futurePos.x - radius + dx), (int) (futurePos.y - radius + dy), (int) (futurePos.x + radius + dx), (int) (futurePos.y + radius + dy));
        }

        /**
         * Gets pos.
         *
         * @return the pos
         */
        public PointF getPos() {
            return pos;
        }

        /**
         * Sets pos.
         *
         * @param desiredPos the desired pos
         */
        public void setPos(PointF desiredPos) {
            PointF direction = new PointF();
            direction.x = desiredPos.x - pos.x;
            direction.y = desiredPos.y - pos.y;

            float magnitude = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);

            if ((magnitude - radius * 2) > 0) {
                direction.x = (direction.x / magnitude) * (magnitude - radius * 2);
                direction.y = (direction.y / magnitude) * (magnitude - radius * 2);

                prevPos.x = pos.x;
                prevPos.y = pos.y;

                pos.offset(direction.x, direction.y);
            }

        }

        /**
         * Gets prev pos.
         *
         * @return the prev pos
         */
        public PointF getPrevPos() {
            return prevPos;
        }

    }

}
