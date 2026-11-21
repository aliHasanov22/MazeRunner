package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * Moving enemy entity that damages the player on contact and patrols
 * neighbouring walkable cells.
 *
 * @author Ali Hasanov
 */
public final class Enemy extends GameObject implements Damaging {

    private static final float SECONDS_PER_STEP = 0.8f;
    private static final Direction[] DIRECTIONS = {
        Direction.RIGHT,
        Direction.DOWN,
        Direction.LEFT,
        Direction.UP
    };

    private final int startX;
    private final int startY;
    private float movementTimer;
    private int directionIndex;

    /** Creates an enemy at (x, y) and remembers its starting cell for resets. */
    public Enemy(int x, int y) {
        super(x, y);
        this.startX = x;
        this.startY = y;
    }

    /** Touching an enemy removes 1 life. */
    @Override
    public int getDamageAmount() {
        return 1;
    }

    /** Moves the enemy one cell every SECONDS_PER_STEP seconds in a basic patrol loop. */
    @Override
    public void update(float deltaSeconds, GameWorld world) {
        movementTimer += deltaSeconds;

        while (movementTimer >= SECONDS_PER_STEP) {
            movementTimer -= SECONDS_PER_STEP;
            boolean moved = false;

            for (int attempt = 0; attempt < DIRECTIONS.length; attempt++) {
                Direction dir = DIRECTIONS[directionIndex];
                directionIndex = (directionIndex + 1) % DIRECTIONS.length;
                GridPosition next = getPosition().move(dir);

                if (world.canEnemyEnter(next, this)) {
                    setPosition(next.x(), next.y());
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                movementTimer = 0.0f;
                break;
            }
        }
    }

    /** Puts the enemy back on its initial spawn cell for a new game. */
    public void resetForNewGame() {
        setPosition(startX, startY);
        movementTimer = 0.0f;
        directionIndex = 0;
    }
}
