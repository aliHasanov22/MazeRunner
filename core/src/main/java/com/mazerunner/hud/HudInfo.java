package com.mazerunner.hud;

import com.mazerunner.map.GridPosition;

public interface HudInfo {

    int getLives();

    int getMaxLives();

    boolean hasKey();

    int getScore();

    float getPlayTime();

    GridPosition getPlayerPosition();

    GridPosition getNearestExit();
}
