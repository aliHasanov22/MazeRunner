package com.mazerunner.entities;

/**
 * A solid wall tile that blocks movement.
 *
 * @author Ali Hasanov
 */
public final class Wall extends GameObject {

    /**
     * Creates a wall tile at the given grid cell.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public Wall(int x, int y) {
        super(x, y);
    }

    /**
     * Always blocks movement regardless of whether the player holds a key.
     *
     * @param hasKey whether the player holds a key (ignored for walls)
     * @return always {@code true}
     */
    @Override
    public boolean blocksMovement(boolean hasKey) {
        return true;
    }
}
