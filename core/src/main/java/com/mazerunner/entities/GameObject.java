package com.mazerunner.entities;

import com.mazerunner.core.GameWorld;
import com.mazerunner.map.GridPosition;

/**
 * Base class for every object on the maze grid. Stores a unique ID and (x, y).
 *
 * @author Ali Hasanov
 */
public abstract class GameObject implements Collidable {

    private static long nextId = 1;

    private final long id;
    private int x;
    private int y;

    /** Creates an object at grid cell (x, y). */
    protected GameObject(int x, int y) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
    }

    /** Returns the unique ID of this object. */
    public long getId() {
        return id;
    }

    /** Returns the x grid coordinate. */
    public int getX() {
        return x;
    }

    /** Returns the y grid coordinate. */
    public int getY() {
        return y;
    }

    /** Returns the position as a GridPosition. */
    public GridPosition getPosition() {
        return new GridPosition(x, y);
    }

    /** Moves this object to another cell. */
    protected void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /** Returns true if this tile blocks movement. Nothing blocks by default. */
    public boolean blocksMovement(boolean hasKey) {
        return false;
    }

    /** Called every frame. Static objects do nothing. */
    public void update(float deltaSeconds, GameWorld world) {
    }

    /** Returns true if both objects are on the same cell. */
    @Override
    public boolean collidesWith(GameObject other) {
        return other != null && x == other.x && y == other.y;
    }
}
