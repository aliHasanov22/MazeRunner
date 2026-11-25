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

    /**
     * Creates an object at the given grid cell and assigns it a unique ID.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    protected GameObject(int x, int y) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the unique ID of this object, used for collision contact tracking.
     *
     * @return unique object ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the horizontal grid coordinate of this object.
     *
     * @return x cell coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertical grid coordinate of this object.
     *
     * @return y cell coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns this object's current coordinates as a {@link GridPosition}.
     *
     * @return current grid position
     */
    public GridPosition getPosition() {
        return new GridPosition(x, y);
    }

    /**
     * Moves this object to another grid cell.
     *
     * @param newX target horizontal grid coordinate
     * @param newY target vertical grid coordinate
     */
    protected void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /**
     * Checks whether this tile blocks movement on the grid.
     * Returns {@code false} by default for walkable objects.
     *
     * @param hasKey {@code true} if the player currently holds at least one key
     * @return {@code true} if movement into this cell is blocked
     */
    public boolean blocksMovement(boolean hasKey) {
        return false;
    }

    /**
     * Updates this object for one frame. Static objects do nothing by default.
     *
     * @param deltaSeconds time elapsed since the last frame in seconds
     * @param world        the active game world
     */
    public void update(float deltaSeconds, GameWorld world) {
    }

    /**
     * Checks whether this object occupies the same grid cell as another object.
     *
     * @param other the other game object to compare positions with
     * @return {@code true} if {@code other} is non-null and on the same cell
     */
    @Override
    public boolean collidesWith(GameObject other) {
        return other != null && x == other.x && y == other.y;
    }
}
