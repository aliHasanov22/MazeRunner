package com.mazerunner.core;

/**
 * Defines the five lifecycle states used by the core gameplay layer.
 * @author Ali Hasanov
 */

public enum GameState {
    RUNNING, //Active gameplay state
    PAUSED,  //temporarily suspended gameplay state
    VICTORY, //Player has reached the exit and won the game
    GAME_OVER,  //Game over state
    MENU   //Main menu state
}
