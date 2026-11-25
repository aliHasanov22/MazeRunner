package com.mazerunner.entities;

import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * The player character: lives, keys, movement speed and power-up timers.
 *
 * @author Ali Hasanov
 */
public final class Player extends GameObject {

    /** Default number of starting lives for a new game. */
    public static final int DEFAULT_LIVES = 3;

    /** Maximum number of lives the player can hold at once. */
    public static final int MAX_LIVES = 5;

    /** Normal walking speed in grid cells per second. */
    public static final float NORMAL_SPEED = 4.0f;

    /** Sprinting speed (when Shift is held) in grid cells per second. */
    public static final float SPRINT_SPEED = 7.0f;

    /** Speed multiplier applied while a speed-boost power-up is active. */
    public static final float SPEED_BOOST_MULTIPLIER = 1.5f;

    private int lives;
    private int keyCount;
    private Direction direction;
    private boolean sprinting;
    private float movementProgress;
    private float speedBoostTime;
    private float shieldTime;

    /**
     * Creates a player at the given grid cell with the specified starting lives.
     *
     * @param x     horizontal starting coordinate
     * @param y     vertical starting coordinate
     * @param lives initial number of lives
     */
    public Player(int x, int y, int lives) {
        super(x, y);
        this.lives = lives;
    }

    /**
     * Returns how many lives the player currently has left.
     *
     * @return remaining lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns how many keys the player has collected in this run.
     *
     * @return collected key count
     */
    public int getKeyCount() {
        return keyCount;
    }

    /**
     * Checks whether the player has collected at least one key.
     *
     * @return {@code true} if {@code keyCount > 0}
     */
    public boolean hasKey() {
        return keyCount > 0;
    }

    /**
     * Checks whether the player currently has an active shield power-up.
     *
     * @return {@code true} while {@code shieldTime > 0}
     */
    public boolean hasShield() {
        return shieldTime > 0;
    }

    /**
     * Returns the remaining shield duration in seconds.
     *
     * @return remaining shield time in seconds
     */
    public float getShieldTime() {
        return shieldTime;
    }

    /**
     * Returns the remaining speed-boost duration in seconds.
     *
     * @return remaining speed-boost time in seconds
     */
    public float getSpeedBoostTime() {
        return speedBoostTime;
    }

    /**
     * Returns the movement direction currently held by the player.
     *
     * @return held {@link Direction}, or {@code null} if stationary
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Updates the held movement direction and sprint flag.
     * Passing {@code null} stops the player and clears accumulated movement progress.
     *
     * @param direction held movement direction, or {@code null} to stop
     * @param sprint    {@code true} if the sprint modifier is held
     */
    public void setMovement(Direction direction, boolean sprint) {
        this.direction = direction;
        this.sprinting = sprint && direction != null;
        if (direction == null) {
            movementProgress = 0;
        }
    }

    /**
     * Accumulates frame time toward the next one-cell step based on walking,
     * sprinting, and active speed-boost multipliers.
     *
     * @param deltaSeconds time elapsed since the last frame in seconds
     */
    public void addMovementTime(float deltaSeconds) {
        if (direction == null) {
            return;
        }
        float speed = sprinting ? SPRINT_SPEED : NORMAL_SPEED;
        if (speedBoostTime > 0) {
            speed *= SPEED_BOOST_MULTIPLIER;
        }
        movementProgress += deltaSeconds * speed;
    }

    /**
     * Checks whether enough movement progress has accumulated to advance one grid cell.
     *
     * @return {@code true} if a direction is held and {@code movementProgress >= 1}
     */
    public boolean canMoveOneCell() {
        return direction != null && movementProgress >= 1;
    }

    /**
     * Moves the player to {@code position} and consumes one cell of movement progress.
     *
     * @param position destination grid cell
     */
    public void stepTo(GridPosition position) {
        setPosition(position.x(), position.y());
        movementProgress -= 1;
    }

    /**
     * Clears leftover movement progress when movement is blocked by a wall or locked exit.
     */
    public void clearMovementTime() {
        movementProgress = 0;
    }

    /**
     * Subtracts up to {@code amount} lives from the player without dropping below zero.
     *
     * @param amount number of lives to remove
     * @return actual number of lives removed
     */
    public int loseLives(int amount) {
        int removed = Math.min(amount, lives);
        lives -= removed;
        return removed;
    }

    /**
     * Adds one life to the player if currently below {@link #MAX_LIVES}.
     *
     * @return {@code true} if a life was added, {@code false} if already at max lives
     */
    public boolean addLife() {
        if (lives >= MAX_LIVES) {
            return false;
        }
        lives++;
        return true;
    }

    /**
     * Increments the player's collected key count by one.
     */
    public void addKey() {
        keyCount++;
    }

    /**
     * Starts or extends the temporary speed-boost timer.
     *
     * @param seconds duration of the speed boost in seconds
     */
    public void activateSpeedBoost(float seconds) {
        speedBoostTime = Math.max(speedBoostTime, seconds);
    }

    /**
     * Starts or extends the temporary shield timer.
     *
     * @param seconds duration of the shield in seconds
     */
    public void activateShield(float seconds) {
        shieldTime = Math.max(shieldTime, seconds);
    }

    /**
     * Counts down active speed-boost and shield timers by {@code deltaSeconds}.
     *
     * @param deltaSeconds time elapsed since the last frame in seconds
     */
    public void updateEffects(float deltaSeconds) {
        speedBoostTime = Math.max(0, speedBoostTime - deltaSeconds);
        shieldTime = Math.max(0, shieldTime - deltaSeconds);
    }

    /**
     * Resets the player's position, lives, key count, held movement, and active
     * power-up timers for a fresh game.
     *
     * @param startX        starting horizontal grid coordinate
     * @param startY        starting vertical grid coordinate
     * @param startingLives initial number of lives to restore
     */
    public void resetForNewGame(int startX, int startY, int startingLives) {
        setPosition(startX, startY);
        lives = startingLives;
        keyCount = 0;
        setMovement(null, false);
        speedBoostTime = 0;
        shieldTime = 0;
    }
}
