package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;

/**
 * Collectable key item needed to unlock the maze exit.
 *
 * @author Ali Hasanov
 */
public final class Key extends GameObject implements Collectable {

    /** Creates a key pickup at (x, y). */
    public Key(int x, int y) {
        super(x, y);
    }

    /** Adds a key to the player, gives 100 score points, and publishes KeyCollectedEvent. */
    @Override
    public void collect(GameWorld world, Player player) {
        player.addKey();
        world.addScore(100, "Key collected");
        world.getEventBus().publish(new GameEvent.KeyCollectedEvent(getPosition(), player.getKeyCount()));
    }
}
