package com.mazerunner.entities;

/**
 * Interface for objects that can collide on the maze grid.
 *
 * @author Ali Hasanov
 */
public interface Collidable {

    /** Returns true if both objects are on the same grid cell. */
    boolean collidesWith(GameObject other);
}
