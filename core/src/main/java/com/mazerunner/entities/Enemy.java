package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * Moving enemy that damages the player on contact. For now it does a simple
 * patrol; the enemy AI (Patrol / Attack / Evade / Retreat) will replace update().
 *
 * @author Ali Hasanov
 */
public final class Enemy extends GameObject implements Damaging {

    private static final float SECONDS_PER_STEP = 0.8f;
    private static final Direction[] PATROL = {Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP};

    private final int startX;
    private final int startY;
    private float timer;
    private int directionIndex;

    /** Creates an enemy at (x, y). */
    public Enemy(int x, int y) {
        super(x, y);
        startX = x;
        startY = y;
    }

    /** Touching an enemy removes 1 life. */
    @Override
    public int getDamageAmount() {
        return 1;
    }

    /** Moves one cell every 0.8 seconds, turning when the way is blocked. */
    @Override
    public void update(float deltaSeconds, GameWorld world) {
        timer += deltaSeconds;
        if (timer < SECONDS_PER_STEP) {
            return;
        }
        timer = 0;
        for (int i = 0; i < PATROL.length; i++) {
            GridPosition next = getPosition().move(PATROL[directionIndex]);
            if (world.canEnemyEnter(next, this)) {
                setPosition(next.x(), next.y());
                return;
            }
            directionIndex = (directionIndex + 1) % PATROL.length;
        }
    }

    /** Puts the enemy back on its starting cell. */
    public void resetForNewGame() {
        setPosition(startX, startY);
        timer = 0;
        directionIndex = 0;
    }
}
