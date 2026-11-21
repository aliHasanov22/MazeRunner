package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;

/**
 * Collectable item that gives the player +1 life (or extra score if already at max lives).
 *
 * @author Ali Hasanov
 */
public final class ExtraLife extends GameObject implements Collectable {

    /** Creates an extra-life pickup at (x, y). */
    public ExtraLife(int x, int y) {
        super(x, y);
    }

    /** Adds 1 life (+150 pts) or converts to +300 pts if the player already has 5 lives. */
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
