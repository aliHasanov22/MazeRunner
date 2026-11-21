package com.mazerunner.entities;

/**
 * A static hazard tile (such as spikes) that damages the player on contact.
 *
 * @author Ali Hasanov
 */
public final class Trap extends GameObject implements Damaging {

    /** Creates a trap tile at (x, y). */
    public Trap(int x, int y) {
        super(x, y);
    }

    /** Touching a trap removes 1 life. */
    @Override
    public int getDamageAmount() {
        return 1;
    }
}
