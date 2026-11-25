package com.mazerunner.entities;

/**
 * Interface for objects that can collide on the maze grid.
 *
 * @author Ali Hasanov
 */
public interface Collidable {

    /**
     * Checks whether this object occupies the same grid cell as another object.
     *
     * @param other the other game object to check against
     * @return {@code true} if both objects are on the same grid cell
     */
    boolean collidesWith(GameObject other);
}
