package com.mazerunner.entities;

/**
 * A static hazard tile (such as spikes) that damages the player on contact.
 *
 * @author Ali Hasanov
 */
public final class Trap extends GameObject implements Damaging {

    /**
     * Creates a static trap tile at the given grid coordinates.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     */
    public Trap(int x, int y) {
        super(x, y);
    }

    /**
     * Returns the life damage dealt when the player steps onto this trap.
     *
     * @return {@code 1} life lost per hit
     */
    @Override
    public int getDamageAmount() {
        return 1;
    }
}
