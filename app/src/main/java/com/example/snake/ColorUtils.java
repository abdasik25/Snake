package com.example.snake;

import android.graphics.Color;

/**
 * The type Color utils.
 */
public class ColorUtils {

    private static int FIRST_COLOR = Color.BLACK;
    private static int SECOND_COLOR = Color.rgb(17, 150, 0);
    private static int THIRD_COLOR = Color.GREEN;

    /**
     * Gets color.
     *
     * @param p the p
     * @return the color
     */
    public static int getColor(float p) {
        int c0;
        int c1;
        if (p <= 0.5f) {
            p *= 2;
            c0 = FIRST_COLOR;
            c1 = SECOND_COLOR;
        } else {
            p = (p - 0.5f) * 2;
            c0 = SECOND_COLOR;
            c1 = THIRD_COLOR;
        }
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);
        return Color.argb(a, r, g, b);
    }

    private static int ave(int src, int dst, float p) {
        return src + java.lang.Math.round(p * (dst - src));
    }
}