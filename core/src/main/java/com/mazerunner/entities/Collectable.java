package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;

/**
 * Interface for items that the player can pick up in the maze.
 *
 * @author Ali Hasanov
 */
public interface Collectable {

    /** Applies the item effect to the world and the player. */
    void collect(GameWorld world, Player player);
}
