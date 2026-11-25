package com.mazerunner.events;

import com.mazerunner.items.PowerUpType;
import com.mazerunner.map.GridPosition;

/**
 * Base interface and event records published by the gameplay layer.
 *
 * @author Ali Hasanov
 */
public interface GameEvent {

    /**
     * Fired when the player loses one or more lives from a hazard.
     *
     * @param source         name of the hazard that caused the damage
     * @param position       grid cell where the damage occurred
     * @param livesRemaining number of lives left after the hit
     * @param livesLost      number of lives removed by the hit
     */
    record LifeLostEvent(String source, GridPosition position, int livesRemaining, int livesLost)
            implements GameEvent { }

    /**
     * Fired when the player picks up a key.
     *
     * @param position  grid cell where the key was collected
     * @param totalKeys total keys held by the player after pickup
     */
    record KeyCollectedEvent(GridPosition position, int totalKeys) implements GameEvent { }

    /**
     * Fired whenever the player's score changes.
     *
     * @param newScore    updated total score
     * @param pointsAdded points added in this change
     * @param reason      description of why the score changed
     */
    record ScoreChangedEvent(int newScore, int pointsAdded, String reason) implements GameEvent { }

    /**
     * Fired when the player picks up an extra-life item.
     *
     * @param livesNow     player's life count after pickup
     * @param lifeWasAdded {@code true} if a life was added, {@code false} if converted to score at max lives
     */
    record ExtraLifeCollectedEvent(int livesNow, boolean lifeWasAdded) implements GameEvent { }

    /**
     * Fired when the player picks up a temporary power-up.
     *
     * @param type            type of power-up collected
     * @param durationSeconds active duration of the effect in seconds
     */
    record PowerUpCollectedEvent(PowerUpType type, float durationSeconds) implements GameEvent { }

    /**
     * Fired when the player reaches an unlocked exit and wins the level.
     *
     * @param finalScore            final score including victory bonuses
     * @param completionTimeSeconds elapsed active play time in seconds
     * @param livesRemaining        lives left at the moment of victory
     */
    record VictoryEvent(int finalScore, float completionTimeSeconds, int livesRemaining)
            implements GameEvent { }

    /**
     * Fired when the player loses all lives and the game ends.
     *
     * @param finalScore player's score when game over occurred
     */
    record GameOverEvent(int finalScore) implements GameEvent { }

    /**
     * Fired when the game world is reset for a new run.
     */
    record NewGameStartedEvent() implements GameEvent { }
}
