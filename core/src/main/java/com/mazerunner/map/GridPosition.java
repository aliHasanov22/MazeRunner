package com.mazerunner.map;

import com.mazerunner.input.Direction;

/**
 * Represents a grid cell coordinate (x, y) in the maze.
 *
 * @author Ali Hasanov
 */
public record GridPosition(int x, int y) {

    /** Returns the neighbouring grid cell one step in the given direction. */
    public GridPosition move(Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        return new GridPosition(x + direction.getDx(), y + direction.getDy());
    }
}
