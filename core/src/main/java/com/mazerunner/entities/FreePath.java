package com.mazerunner.entities;

/**
 * A normal walkable floor cell in the maze.
 *
 * @author Ali Hasanov
 */
public final class FreePath extends GameObject {

    /**
     * Creates a walkable floor cell at the given grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public FreePath(int x, int y) {
        super(x, y);
    }
}
