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

    /**
     * Creates a power-up pickup at the given grid cell with a type and duration.
     *
     * @param x               horizontal grid coordinate
     * @param y               vertical grid coordinate
     * @param type            power-up effect type to apply on pickup
     * @param durationSeconds active effect duration in seconds
     */
    public PowerUp(int x, int y, PowerUpType type, float durationSeconds) {
        super(x, y);
        this.type = type;
        this.durationSeconds = durationSeconds;
    }

    /**
     * Returns the effect type of this power-up.
     *
     * @return the {@link PowerUpType} granted on pickup
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * Returns how long this power-up's effect lasts once collected.
     *
     * @return effect duration in seconds
     */
    public float getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Activates the speed boost or shield on the player, awards 200 score points,
     * and publishes a {@link GameEvent.PowerUpCollectedEvent}.
     *
     * @param world  the active game world
     * @param player the player collecting the power-up
     */
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
