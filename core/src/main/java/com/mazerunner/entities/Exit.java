package com.mazerunner.entities;

/**
 * The exit tile on the border of the maze.
 * Stays locked like a wall until the player picks up a key.
 *
 * @author Ali Hasanov
 */
public final class Exit extends GameObject {

    /**
     * Creates an exit tile at the given border grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public Exit(int x, int y) {
        super(x, y);
    }

    /**
     * Blocks movement until the player has collected at least one key.
     *
     * @param hasKey {@code true} if the player currently holds a key
     * @return {@code true} when locked ({@code !hasKey}), {@code false} when unlocked
     */
    @Override
    public boolean blocksMovement(boolean hasKey) {
        return !hasKey;  
        
    }
}
