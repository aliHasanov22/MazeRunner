package com.mazerunner.entities;

/**
 * Interface for hazards that remove lives when the player touches them.
 *
 * @author Ali Hasanov
 */
public interface Damaging {

    /** Returns how many lives are lost from one hit. */
    int getDamageAmount();
}
