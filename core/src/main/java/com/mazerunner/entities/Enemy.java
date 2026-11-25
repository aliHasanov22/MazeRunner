package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridPosition;

/**
 * Moving enemy entity that damages the player on contact and patrols
 * neighbouring walkable maze cells.
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

    /**
     * Creates an enemy at the given grid coordinates and records its spawn cell.
     *
     * @param x initial horizontal grid coordinate
     * @param y initial vertical grid coordinate
     */
    public Enemy(int x, int y) {
        super(x, y);
        startX = x;
        startY = y;
    }

    /**
     * Returns the number of lives removed when the enemy hits the player.
     *
     * @return {@code 1} life lost per hit
     */
    @Override
    public int getDamageAmount() {
        return 1;
    }

    /**
     * Advances the patrol timer and moves one walkable cell every 0.8 seconds,
     * cycling directions clockwise when blocked.
     *
     * @param deltaSeconds time elapsed since the last frame in seconds
     * @param world        the active game world used to check legal enemy steps
     */
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

    /**
     * Restores the enemy to its initial spawn cell and resets its patrol state.
     */
    public void resetForNewGame() {
        setPosition(startX, startY);
        timer = 0;
        directionIndex = 0;
    }
}
