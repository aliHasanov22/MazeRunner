package com.mazerunner.input;

/**
 * The four movement directions on the maze grid and their (dx, dy) step offsets.
 *
 * @author Ali Hasanov
 */
public enum Direction {
    /** Moves one cell upward ({@code y + 1}). */
    UP(0, 1),

    /** Moves one cell downward ({@code y - 1}). */
    DOWN(0, -1),

    /** Moves one cell left ({@code x - 1}). */
    LEFT(-1, 0),

    /** Moves one cell right ({@code x + 1}). */
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns the horizontal cell offset for one step in this direction.
     *
     * @return change in x ({@code -1}, {@code 0}, or {@code 1})
     */
    public int getDx() {
        return dx;
    }

    /**
     * Returns the vertical cell offset for one step in this direction.
     *
     * @return change in y ({@code -1}, {@code 0}, or {@code 1})
     */
    public int getDy() {
        return dy;
    }
}
