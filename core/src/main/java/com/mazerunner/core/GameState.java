package com.mazerunner.core;

/**
 * Defines the five lifecycle states used by the core gameplay layer.
 *
 * @author Ali Hasanov
 */
public enum GameState {
    /** Active gameplay state where timers, movement, enemies, and collisions update. */
    RUNNING,

    /** Temporarily suspended gameplay state with timers and movement frozen. */
    PAUSED,

    /** End state reached when the player enters an unlocked exit and wins the level. */
    VICTORY,

    /** End state reached when the player runs out of lives. */
    GAME_OVER,

    /** Idle state indicating the game has returned to the main menu. */
    MENU
}
