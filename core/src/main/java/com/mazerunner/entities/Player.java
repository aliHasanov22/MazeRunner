package com.mazerunner.entities;

import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * Represents the player character, including lives, keys, movement speed,
 * and active power-up timers.
 *
 * @author Ali Hasanov
 */
public final class Player extends GameObject {

    public static final int DEFAULT_LIVES = 3;
    public static final int MAX_LIVES = 5;
    public static final float NORMAL_SPEED = 4.0f;
    public static final float SPRINT_SPEED = 7.0f;
    public static final float SPEED_BOOST_MULTIPLIER = 1.5f;

    private int lives;
    private int keyCount;
    private Direction requestedDirection;
    private boolean sprinting;
    private float movementProgress;
    private float speedBoostTime;
    private float shieldTime;

    /** Creates a player at (x, y) with the default 3 lives. */
    public Player(int x, int y) {
        this(x, y, DEFAULT_LIVES);
    }

    /** Creates a player at (x, y) with the given starting lives (1 to 5). */
    public Player(int x, int y, int startingLives) {
        super(x, y);
        if (startingLives < 1 || startingLives > MAX_LIVES) {
            throw new IllegalArgumentException("Starting lives must be between 1 and " + MAX_LIVES);
        }
        this.lives = startingLives;
    }

    /** Returns how many lives the player has left. */
    public int getLives() {
        return lives;
    }

    /** Returns how many keys the player has collected. */
    public int getKeyCount() {
        return keyCount;
    }

    /** Returns true if the player has collected at least one key. */
    public boolean hasKey() {
        return keyCount > 0;
    }

    /** Returns true while the shield power-up timer is active. */
    public boolean hasShield() {
        return shieldTime > 0.0f;
    }

    /** Returns the remaining shield time in seconds. */
    public float getShieldTime() {
        return shieldTime;
    }

    /** Returns the remaining speed-boost time in seconds. */
    public float getSpeedBoostTime() {
        return speedBoostTime;
    }

    /** Sets the held movement direction and sprint state. Passing null stops movement. */
    public void setMovement(Direction direction, boolean sprint) {
        this.requestedDirection = direction;
        this.sprinting = sprint && direction != null;
        if (direction == null) {
            this.movementProgress = 0.0f;
        }
    }

    /** Returns the direction currently held by the player, or null if stopped. */
    public Direction getRequestedDirection() {
        return requestedDirection;
    }

    /** Adds frame time to the player's movement progress based on current speed. */
    public void addMovementTime(float deltaSeconds) {
        if (requestedDirection != null) {
            float speed = sprinting ? SPRINT_SPEED : NORMAL_SPEED;
            if (speedBoostTime > 0.0f) {
                speed = speed * SPEED_BOOST_MULTIPLIER;
            }
            movementProgress += deltaSeconds * speed;
        }
    }

    /** Returns true when enough movement progress has built up to step one grid cell. */
    public boolean canMoveOneCell() {
        return requestedDirection != null && movementProgress >= 1.0f;
    }

    /** Subtracts one cell worth of movement progress after a step. */
    public void consumeMovementStep() {
        movementProgress -= 1.0f;
        if (movementProgress < 0.0f) {
            movementProgress = 0.0f;
        }
    }

    /** Resets movement progress when the player is blocked by a wall or locked exit. */
    public void clearMovementTime() {
        movementProgress = 0.0f;
    }

    /** Moves the player to the given grid cell. */
    public void moveTo(GridPosition position) {
        setPosition(position.x(), position.y());
    }

    /** Removes up to the given number of lives and returns how many were actually removed. */
    public int loseLives(int amount) {
        if (amount <= 0 || lives <= 0) {
            return 0;
        }
        int removed = Math.min(amount, lives);
        lives -= removed;
        return removed;
    }

    /** Adds 1 life if below MAX_LIVES (5), returning true if a life was added. */
    public boolean addLife() {
        if (lives >= MAX_LIVES) {
            return false;
        }
        lives++;
        return true;
    }

    /** Adds 1 key to the player's inventory. */
    public void addKey() {
        keyCount++;
    }

    /** Activates or extends the speed boost timer. */
    public void activateSpeedBoost(float durationSeconds) {
        if (durationSeconds > speedBoostTime) {
            speedBoostTime = durationSeconds;
        }
    }

    /** Activates or extends the shield timer. */
    public void activateShield(float durationSeconds) {
        if (durationSeconds > shieldTime) {
            shieldTime = durationSeconds;
        }
    }

    /** Counts down active power-up timers each frame. */
    public void updateEffects(float deltaSeconds) {
        speedBoostTime -= deltaSeconds;
        if (speedBoostTime < 0.0f) {
            speedBoostTime = 0.0f;
        }
        shieldTime -= deltaSeconds;
        if (shieldTime < 0.0f) {
            shieldTime = 0.0f;
        }
    }

    /** Clears held movement and active power-up timers. */
    public void resetTemporaryState() {
        requestedDirection = null;
        sprinting = false;
        movementProgress = 0.0f;
        speedBoostTime = 0.0f;
        shieldTime = 0.0f;
    }

    /** Resets position, lives, keys, and temporary effects for a new game. */
    public void resetForNewGame(int startX, int startY, int startingLives) {
        setPosition(startX, startY);
        this.lives = startingLives;
        this.keyCount = 0;
        resetTemporaryState();
    }
}
