package com.mazerunner.map;

import com.mazerunner.input.Direction;

/**
 * A grid cell coordinate (x, y).
 *
 * @author Ali Hasanov
 */
public record GridPosition(int x, int y) {

    /** Returns the neighbouring cell in the given direction. */
    public GridPosition move(Direction direction) {
        return new GridPosition(x + direction.getDx(), y + direction.getDy());
    }
}
