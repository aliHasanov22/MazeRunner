package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.map.GridPosition;

/**
 * Base class for all objects placed on the maze grid.
 * Stores a unique ID and grid coordinates (x, y).
 *
 * @author Ali Hasanov
 */
public abstract class GameObject implements Collidable {

    private static long nextId = 1;

    private final long id;
    private int x;
    private int y;

    /**
     * Creates a game object at the given grid cell.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    protected GameObject(int x, int y) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
    }

    /** Returns the unique ID of this object. */
    public long getId() {
        return id;
    }

    /** Returns the horizontal grid coordinate. */
    public int getX() {
        return x;
    }

    /** Returns the vertical grid coordinate. */
    public int getY() {
        return y;
    }

    /** Returns the current grid position as a GridPosition object. */
    public GridPosition getPosition() {
        return new GridPosition(x, y);
    }

    /** Moves this object to a new grid cell. */
    protected void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /** Returns true if this tile blocks movement. */
    public boolean blocksMovement(boolean hasKey) {
        return false;
    }

    /** Updates this object for one frame. Static objects do nothing by default. */
    public void update(float deltaSeconds, GameWorld world) {
        // overridden by moving objects like Enemy
    }

    /** Checks if this object is on the same grid cell as another object. */
    @Override
    public boolean collidesWith(GameObject other) {
        return other != null && this.x == other.x && this.y == other.y;
    }
}
