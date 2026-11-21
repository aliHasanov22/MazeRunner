package com.mazerunner.entities;

/**
 * A solid wall tile that blocks movement.
 *
 * @author Ali Hasanov
 */
public final class Wall extends GameObject {

    /** Creates a wall tile at (x, y). */
    public Wall(int x, int y) {
        super(x, y);
    }

    /** Walls always block movement. */
    @Override
    public boolean blocksMovement(boolean hasKey) {
        return true;
    }
}
