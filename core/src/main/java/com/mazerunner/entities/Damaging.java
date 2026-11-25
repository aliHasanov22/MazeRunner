package com.mazerunner.entities;

/**
 * Interface for hazards that remove lives when the player touches them.
 *
 * @author Ali Hasanov
 */
public interface Damaging {

    /**
     * Returns the number of lives removed from the player by one logical hit.
     *
     * @return lives removed per hit
     */
    int getDamageAmount();
}
