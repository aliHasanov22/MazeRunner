package com.mazerunner.input;

/**
 * The four movement directions on the maze grid and their (dx, dy) step offsets.
 *
 * @author Ali Hasanov
 */
public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /** Returns the change in x for one step in this direction. */
    public int getDx() {
        return dx;
    }

    /** Returns the change in y for one step in this direction. */
    public int getDy() {
        return dy;
    }
}
