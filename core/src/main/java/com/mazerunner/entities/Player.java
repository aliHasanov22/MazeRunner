package com.mazerunner.entities;

import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * The player character: lives, keys, movement speed and power-up timers.
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
    private Direction direction;
    private boolean sprinting;
    private float movementProgress;
    private float speedBoostTime;
    private float shieldTime;

    /** Creates a player at (x, y) with the given number of lives. */
    public Player(int x, int y, int lives) {
        super(x, y);
        this.lives = lives;
    }

    /** Returns how many lives the player has left. */
    public int getLives() {
        return lives;
    }

    /** Returns how many keys the player has collected. */
    public int getKeyCount() {
        return keyCount;
    }

    /** Returns true if the player has at least one key. */
    public boolean hasKey() {
        return keyCount > 0;
    }

    /** Returns true while the shield is active. */
    public boolean hasShield() {
        return shieldTime > 0;
    }

    /** Returns the remaining shield time in seconds. */
    public float getShieldTime() {
        return shieldTime;
    }

    /** Returns the remaining speed boost time in seconds. */
    public float getSpeedBoostTime() {
        return speedBoostTime;
    }

    /** Returns the direction the player is holding, or null if not moving. */
    public Direction getDirection() {
        return direction;
    }

    /** Sets the held direction and sprint state. Null stops the player. */
    public void setMovement(Direction direction, boolean sprint) {
        this.direction = direction;
        this.sprinting = sprint && direction != null;
        if (direction == null) {
            movementProgress = 0;
        }
    }

    /** Adds frame time to the movement progress. One full point means one cell. */
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

    /** Returns true when the player has built up enough progress to move one cell. */
    public boolean canMoveOneCell() {
        return direction != null && movementProgress >= 1;
    }

    /** Moves the player one cell and uses up one cell of movement progress. */
    public void stepTo(GridPosition position) {
        setPosition(position.x(), position.y());
        movementProgress -= 1;
    }

    /** Throws away leftover movement progress (used when blocked by a wall). */
    public void clearMovementTime() {
        movementProgress = 0;
    }

    /** Removes lives and returns how many were actually removed. */
    public int loseLives(int amount) {
        int removed = Math.min(amount, lives);
        lives -= removed;
        return removed;
    }

    /** Adds one life if below MAX_LIVES. Returns true if a life was added. */
    public boolean addLife() {
        if (lives >= MAX_LIVES) {
            return false;
        }
        lives++;
        return true;
    }

    /** Adds one key to the player. */
    public void addKey() {
        keyCount++;
    }

    /** Starts or extends the speed boost. */
    public void activateSpeedBoost(float seconds) {
        speedBoostTime = Math.max(speedBoostTime, seconds);
    }

    /** Starts or extends the shield. */
    public void activateShield(float seconds) {
        shieldTime = Math.max(shieldTime, seconds);
    }

    /** Counts down the power-up timers. */
    public void updateEffects(float deltaSeconds) {
        speedBoostTime = Math.max(0, speedBoostTime - deltaSeconds);
        shieldTime = Math.max(0, shieldTime - deltaSeconds);
    }

    /** Resets position, lives, keys, movement and power-ups for a new game. */
    public void resetForNewGame(int startX, int startY, int startingLives) {
        setPosition(startX, startY);
        lives = startingLives;
        keyCount = 0;
        setMovement(null, false);
        speedBoostTime = 0;
        shieldTime = 0;
    }
}
