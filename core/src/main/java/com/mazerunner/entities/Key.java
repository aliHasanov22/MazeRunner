package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;

/**
 * Collectable key item needed to unlock the maze exit.
 *
 * @author Ali Hasanov
 */
public final class Key extends GameObject implements Collectable {

    /**
     * Creates a key pickup at the given grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public Key(int x, int y) {
        super(x, y);
    }

    /**
     * Adds one key to the player's inventory, awards 100 score points, and
     * publishes a {@link GameEvent.KeyCollectedEvent}.
     *
     * @param world  the active game world
     * @param player the player collecting the key
     */
    @Override
    public void collect(GameWorld world, Player player) {
        player.addKey();
        world.addScore(100, "Key collected");
        world.getEventBus().publish(new GameEvent.KeyCollectedEvent(getPosition(), player.getKeyCount()));
    }
}
