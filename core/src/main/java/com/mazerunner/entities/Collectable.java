package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;

/**
 * Interface for items that the player can pick up in the maze.
 *
 * @author Ali Hasanov
 */
public interface Collectable {

    /**
     * Applies this item's pickup effect to the active game world and player.
     *
     * @param world  the active game world
     * @param player the player collecting this item
     */
    void collect(GameWorld world, Player player);
}
