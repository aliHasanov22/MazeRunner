package com.mazerunner.entities;

/**
 * The exit tile on the border of the maze.
 * Stays locked like a wall until the player picks up a key.
 *
 * @author Ali Hasanov
 */
public final class Exit extends GameObject {

    /** Creates an exit tile at (x, y). */
    public Exit(int x, int y) {
        super(x, y);
    }

    /** Blocks movement until the player has at least one key. */
    @Override
    public boolean blocksMovement(boolean hasKey) {
        return !hasKey;
    }
}
