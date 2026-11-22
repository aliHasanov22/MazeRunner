package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;
import com.mazerunner.items.PowerUpType;

/**
 * Item that gives a temporary speed boost or shield.
 *
 * @author Ali Hasanov
 */
public final class PowerUp extends GameObject implements Collectable {

    private final PowerUpType type;
    private final float durationSeconds;

    /** Creates a power-up at (x, y) with a type and duration in seconds. */
    public PowerUp(int x, int y, PowerUpType type, float durationSeconds) {
        super(x, y);
        this.type = type;
        this.durationSeconds = durationSeconds;
    }

    /** Returns the power-up type. */
    public PowerUpType getType() {
        return type;
    }

    /** Returns how long the effect lasts in seconds. */
    public float getDurationSeconds() {
        return durationSeconds;
    }

    /** Activates the effect, adds 200 points and publishes a PowerUpCollectedEvent. */
    @Override
    public void collect(GameWorld world, Player player) {
        if (type == PowerUpType.SPEED_BOOST) {
            player.activateSpeedBoost(durationSeconds);
        } else {
            player.activateShield(durationSeconds);
        }
        world.addScore(200, "Power-up collected");
        world.getEventBus().publish(new GameEvent.PowerUpCollectedEvent(type, durationSeconds));
    }
}
