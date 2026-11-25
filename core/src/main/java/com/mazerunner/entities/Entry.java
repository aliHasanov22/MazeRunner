package com.mazerunner.entities;

/**
 * The starting tile where the player spawns.
 *
 * @author Ali Hasanov
 */
public final class Entry extends GameObject {

    /**
     * Creates the player starting entry tile at the given grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public Entry(int x, int y) {
        super(x, y);
    }
}
