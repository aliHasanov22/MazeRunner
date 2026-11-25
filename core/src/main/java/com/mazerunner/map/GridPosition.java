package com.mazerunner.map;

import com.mazerunner.input.Direction;

/**
 * Immutable grid cell coordinate {@code (x, y)} on the maze map.
 *
 * @param x horizontal grid column coordinate
 * @param y vertical grid row coordinate
 * @author Ali Hasanov
 */
public record GridPosition(int x, int y) {

    /**
     * Returns the neighbouring cell one step away in the given direction.
     *
     * @param direction movement direction to step toward
     * @return new {@link GridPosition} offset by {@code direction}
     */
    public GridPosition move(Direction direction) {
        return new GridPosition(x + direction.getDx(), y + direction.getDy());
    }
}
