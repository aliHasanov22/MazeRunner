package com.mazerunner.events;

import com.mazerunner.items.PowerUpType;
import com.mazerunner.map.GridPosition;

/**
 * Base interface and event records published by the gameplay layer.
 *
 * @author Ali Hasanov
 */
public interface GameEvent {

    /** Fired when the player loses one or more lives from a hazard. */
    record LifeLostEvent(String source, GridPosition position, int livesRemaining, int livesLost)
            implements GameEvent { }

    /** Fired when the player picks up a key. */
    record KeyCollectedEvent(GridPosition position, int totalKeys) implements GameEvent { }

    /** Fired whenever the player's score changes. */
    record ScoreChangedEvent(int newScore, int pointsAdded, String reason) implements GameEvent { }

    /** Fired when the player picks up an extra-life item. */
    record ExtraLifeCollectedEvent(int livesNow, boolean lifeWasAdded) implements GameEvent { }

    /** Fired when the player picks up a temporary power-up. */
    record PowerUpCollectedEvent(PowerUpType type, float durationSeconds) implements GameEvent { }

    /** Fired when the player reaches an unlocked exit and wins. */
    record VictoryEvent(int finalScore, float completionTimeSeconds, int livesRemaining)
            implements GameEvent { }

    /** Fired when the player loses all lives. */
    record GameOverEvent(int finalScore) implements GameEvent { }

    /** Fired when the game is reset for a new run. */
    record NewGameStartedEvent() implements GameEvent { }
}
