package com.example.snake;

import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The interface Snake behavior.
 */
public interface SnakeBehavior {

    /**
     * Add to fruit list.
     *
     * @param view the view
     */
    void addToFruitList(View view);

    /**
     * Gets fruits.
     *
     * @return the fruits
     */
    List<View> getFruits();

    /**
     * Collect fruit.
     *
     * @param apple the apple
     */
    void CollectFruit(View apple);

    /**
     * Add to trash list.
     *
     * @param view the view
     */
    void addToTrashList(View view);

    /**
     * Gets trash.
     *
     * @return the trash
     */
    List<View> getTrash();

    /**
     * Remove trash.
     *
     * @param view the view
     */
    void removeTrash(View view);
}
