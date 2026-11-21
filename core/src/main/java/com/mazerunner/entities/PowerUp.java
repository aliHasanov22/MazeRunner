package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.events.GameEvent;
import com.mazerunner.items.PowerUpType;

/**
 * Collectable power-up that grants a temporary speed boost or shield.
 *
 * @author Ali Hasanov
 */
public final class PowerUp extends GameObject implements Collectable {

    private final PowerUpType type;
    private final float durationSeconds;

    /** Creates a power-up at (x, y) with the given type and duration in seconds. */
    public PowerUp(int x, int y, PowerUpType type, float durationSeconds) {
        super(x, y);
        if (type == null) {
            throw new IllegalArgumentException("Power-up type cannot be null");
        }
        if (durationSeconds <= 0.0f) {
            throw new IllegalArgumentException("Power-up duration must be positive");
        }
        this.type = type;
        this.durationSeconds = durationSeconds;
    }

    /** Returns the type of this power-up. */
    public PowerUpType getType() {
        return type;
    }

    /** Returns how long the effect lasts in seconds. */
    public float getDurationSeconds() {
        return durationSeconds;
    }

    /** Activates the power-up on the player, adds 200 points, and publishes PowerUpCollectedEvent. */
    @Override
    public void collect(GameWorld world, Player player) {
        if (type == PowerUpType.SPEED_BOOST) {
            player.activateSpeedBoost(durationSeconds);
        } else if (type == PowerUpType.SHIELD) {
            player.activateShield(durationSeconds);
        }
        world.addScore(200, "Power-up collected");
        world.getEventBus().publish(new GameEvent.PowerUpCollectedEvent(type, durationSeconds));
    }
}
