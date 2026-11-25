package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;

/**
 * Collectable item that gives the player +1 life (or extra score if already at max lives).
 *
 * @author Ali Hasanov
 */
public final class ExtraLife extends GameObject implements Collectable {

    /**
     * Creates an extra-life pickup at the given grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public ExtraLife(int x, int y) {
        super(x, y);
    }

    /**
     * Adds one life and awards 150 points, or awards 300 points if the player
     * is already at {@link Player#MAX_LIVES}, then publishes an
     * {@link GameEvent.ExtraLifeCollectedEvent}.
     *
     * @param world  the active game world
     * @param player the player collecting the item
     */
    @Override
    public void collect(GameWorld world, Player player) {
        boolean added = player.addLife();
        if (added) {
            world.addScore(150, "Extra life collected");
        } else {
            world.addScore(300, "Extra life converted to score");
        }
        world.getEventBus().publish(new GameEvent.ExtraLifeCollectedEvent(player.getLives(), added));
    }
}
